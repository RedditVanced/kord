import dev.kord.common.entity.ActivityType
import dev.kord.common.entity.DiscordBotActivity
import dev.kord.common.entity.PresenceStatus
import dev.kord.common.ratelimit.BucketRateLimiter
import dev.kord.gateway.*
import dev.kord.gateway.retry.LinearRetry
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

suspend fun main(args: Array<String>) {
    val token = args.firstOrNull() ?: error("expected a token")

    val gateway = DefaultGateway {
        client = HttpClient(CIO) {
            install(WebSockets)
            install(ContentNegotiation) {
                json()
            }
        }

        reconnectRetry = LinearRetry(2.seconds, 20.seconds, 10)
        sendRateLimiter = BucketRateLimiter(120, 60.seconds)
    }

    gateway.events.filterIsInstance<MessageCreate>().flowOn(Dispatchers.Default).onEach {
        val words = it.message.content.split(' ')
        when (words.firstOrNull()) {
            "!close" -> gateway.stop()
            "!detach" -> gateway.detach()
            "!status" -> when (words.getOrNull(1)) {
                "playing" -> gateway.send(
                    UpdateStatus(
                        status = PresenceStatus.Online,
                        afk = false,
                        activities = listOf(DiscordBotActivity("Kord", ActivityType.Game)),
                        since = null
                    )
                )
            }
            "!ping" -> gateway.send(
                UpdateStatus(
                    status = PresenceStatus.Online,
                    afk = false,
                    activities = listOf(
                        DiscordBotActivity(
                            "Ping is ${gateway.ping.value?.inWholeMilliseconds}",
                            ActivityType.Game
                        )
                    ),
                    since = null
                )
            )
        }
    }.launchIn(@OptIn(DelicateCoroutinesApi::class) GlobalScope)

    gateway.start(token) {
        @OptIn(PrivilegedIntent::class)
        intents = Intents.all
    }
}
