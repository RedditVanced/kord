package dev.kord.voice.gateway

import dev.kord.common.entity.Snowflake
import dev.kord.gateway.*
import dev.kord.voice.command.*
import dev.kord.voice.event.HelloVoiceEvent
import dev.kord.voice.event.ReadyVoiceEvent
import dev.kord.voice.event.SessionDescription
import dev.kord.voice.event.VoiceEvent
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import me.uport.knacl.nacl
import mu.KotlinLogging
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates
import kotlin.time.Duration

private val defaultVoiceGatewayLogger = KotlinLogging.logger {}

data class VoiceOptions(
    val guildId: Snowflake,
    val channelId: Snowflake,
    val selfMute: Boolean = false,
    val selfDeaf: Boolean = false
)

private val LOG = KotlinLogging.logger { }

@OptIn(ObsoleteCoroutinesApi::class)
class DefaultVoiceGateway(val gateway: Gateway, val client: HttpClient, val voiceOpitons: VoiceOptions) : VoiceGateway {

    private lateinit var udp: ConnectedDatagramSocket

    private lateinit var socket: DefaultClientWebSocketSession


    private val ticker = Ticker()

    val _events = MutableSharedFlow<VoiceEvent>()
    override val events: SharedFlow<VoiceEvent> = _events

    private val sequence = atomic<Short>(0)

    internal var ssrc by Delegates.notNull<Int>()
    private lateinit var network: NetworkAddress

    private lateinit var secretKey: List<Int>

    var ready: Boolean = false
        private set


    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val rtpHeader
        get() = ByteBuffer.allocate(12)
            .put(0x80.toByte())
            .put(0x78)
            .putShort(sequence.getAndUpdate { (it + 1).toShort() })
            .putInt(sequence.value * 960)
            .putInt(ssrc!!)
            .array()


    override suspend fun connect() {

        val (voiceStateUpdateVoice, serverUpdateEvent) = retrieveVoiceServerInformation()

        heartbeat()

        val voiceReadyEvent = establishConnection(voiceStateUpdateVoice, serverUpdateEvent)


        ssrc = voiceReadyEvent.ssrc
        network = NetworkAddress(voiceReadyEvent.ip, voiceReadyEvent.port)
        udp = aSocket(ActorSelectorManager(Dispatchers.IO)).udp().connect(network)

        LOG.debug { "Discovering ip address" }
        val externalNetwork = ipDiscovery(
            ssrc,
            network
        )
        LOG.debug { "Discovered network: $externalNetwork" }


        val sessionDescriptionWaiter: Deferred<SessionDescription> = waitFor()

        send(
            VoiceSelectProtocolCommand(
                "udp",
                VoiceSelectProtocolCommandData(externalNetwork.hostname, externalNetwork.port, "xsalsa20_poly1305")
            )
        )

        val sessionDescription = sessionDescriptionWaiter.await()
        LOG.debug { "Got session descriptor => voice gateway is ready" }
        secretKey = sessionDescription.secretKey
        ready = true

    }

    override suspend fun resume() {
        TODO("Not implemented yet")
    }


    private suspend fun trySend(command: VoiceCommand): Boolean {
        return try {
            sendUnsafe(command)
            true
        } catch (_: Exception) {
            false
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private suspend fun sendUnsafe(command: VoiceCommand) {
        val json = Json.encodeToString(VoiceCommand, command)
        defaultVoiceGatewayLogger.trace { "Gateway >>> $json" }
        socket.send(json)
    }


    override suspend fun send(command: VoiceCommand): Boolean {
        return trySend(command)
    }


    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default

    private suspend fun sendUpdateVoiceStatus(voiceOptions: VoiceOptions) {
        gateway.send(
            UpdateVoiceStatus(
                guildId = voiceOptions.guildId,
                channelId = voiceOptions.channelId,
                selfDeaf = voiceOptions.selfDeaf,
                selfMute = voiceOptions.selfMute
            )
        )
    }

    private suspend fun retrieveVoiceServerInformation(): Pair<VoiceStateUpdate, VoiceServerUpdate> {
        val voiceStateUpdateEventWaiter: Deferred<VoiceStateUpdate> = waitForTCPGatewayAsync()
        val serverUpdateEventWaiter: Deferred<VoiceServerUpdate> = waitForTCPGatewayAsync()

        sendUpdateVoiceStatus(voiceOpitons)

        val voiceStateUpdateVoice = voiceStateUpdateEventWaiter.await()
        val serverUpdateEvent = serverUpdateEventWaiter.await()

        return Pair(voiceStateUpdateVoice, serverUpdateEvent)
    }

    private suspend fun webSocket(endpoint: String): DefaultClientWebSocketSession {
        return client.webSocketSession { url("wss://$endpoint") }

    }

    private suspend fun ipDiscovery(ssrc: Int, address: NetworkAddress, tries: Int = 0): NetworkAddress {
        val buffer = ByteBuffer.allocate(70)
            .putShort(1)
            .putShort(70)
            .putInt(ssrc)
            .array()
        val datagram = Datagram(ByteReadPacket(buffer, 0, buffer.size), address)
        udp.send(datagram)
        LOG.debug { "waiting for ip frame" }
        val received = try {
            withTimeout(Duration.seconds(1)) { udp.incoming.receive().packet }
        } catch (e: TimeoutCancellationException) {
            if (tries < 5) {
                val nextTry = tries + 1
                LOG.warn { "Ip discovery failed first try. Retrying $nextTry/5" }
                return ipDiscovery(ssrc, address, nextTry)
            } else {
                throw e
            }
        }

        LOG.debug { "got ip frame" }
        received.discardExact(4)
        val ip = received.readBytes(received.remaining.toInt() - 2).toString().trim()
        val port = received.readShortLittleEndian().toInt()

        return NetworkAddress(ip, port)
    }

    private suspend fun establishConnection(
        voiceState: VoiceStateUpdate,
        voiceServerUpdate: VoiceServerUpdate
    ): ReadyVoiceEvent {
        val endpoint = voiceServerUpdate.voiceServerUpdateData.endpoint ?: error("No endpoint recieved.")
        socket = webSocket(endpoint)
        launch {
            readSocket()
        }
        val readyEventWaiter = waitFor<ReadyVoiceEvent>()
        send(
            VoiceIdentifyCommand(
                voiceState.voiceState.guildId.value!!.asString,
                voiceState.voiceState.userId.asString,
                voiceState.voiceState.sessionId,
                voiceServerUpdate.voiceServerUpdateData.token

            )
        )
        return readyEventWaiter.await()
    }


    private suspend fun heartbeat() {
        on<HelloVoiceEvent> {
            ticker.tickAt(heartbeatInterval) {
                send(VoiceHeartbeatCommand(Clock.System.now().epochSeconds))
            }
        }

    }

    override suspend fun sendEncryptedVoice(data: ByteArray) {
        val currentRtpHeader = rtpHeader
        val nonce = currentRtpHeader.copyOf(24)
        val encrypted = nacl.secretbox.seal(data, nonce, secretKey.map { it.toByte() }.toByteArray())
        defaultVoiceGatewayLogger.trace { "Gateway >>> packet" }

        udp.send(Datagram(ByteReadPacket(currentRtpHeader + encrypted), network))
    }

    private suspend inline fun <reified T> waitForTCPGatewayAsync(): Deferred<T> = async {
        gateway.events.filterIsInstance<T>().take(1).first()
    }


    private suspend inline fun <reified T> waitFor(): Deferred<T> = async {
        events.filterIsInstance<T>().take(1).first()
    }


    private suspend fun readSocket() {
        socket.incoming.receiveAsFlow().buffer(Channel.UNLIMITED).collect {
            when (it) {
                is Frame.Binary, is Frame.Text -> read(it)
                else -> { /*ignore*/
                    LOG.debug { "Received unexpected frame: $it" }
                }
            }
        }

    }

    private suspend fun read(frame: Frame) {
        val json = String(frame.data, Charsets.UTF_8)

        try {
            defaultVoiceGatewayLogger.trace { "Gateway <<< $json" }
            val event = jsonParser.decodeFromString(VoiceEvent.Companion, json) ?: return
            _events.emit(event)
        } catch (exception: Exception) {
            defaultVoiceGatewayLogger.error(exception)
        }

    }


}