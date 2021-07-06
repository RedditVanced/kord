import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.voice.gateway.DefaultVoiceGateway
import dev.kord.voice.gateway.VoiceGateway
import dev.kord.voice.gateway.VoiceOptions
import kotlinx.coroutines.launch


private val voiceGateways = mutableMapOf<Snowflake, VoiceGateway>()
private val playerManager = DefaultAudioPlayerManager().also {
    AudioSourceManagers.registerRemoteSources(it)
}

suspend fun main(args: Array<String>) {
    val kord = Kord(args.firstOrNull() ?: error("token required"))


    kord.on<MessageCreateEvent> {
        if (message.author?.isBot == true) return@on
        if (message.content == "!ping") message.channel.createMessage("pong")
        if (message.content == "!join") joinChannel()
    }


    kord.login { playing("!ping to pong") }
}

suspend fun MessageCreateEvent.joinChannel() {
    val channel = member!!.getVoiceState().channelId?: kotlin.run {
        message.reply { content = "Not in VC!" }
        return
    }
    val options = VoiceOptions(guildId!!, channel)
    val gateway = DefaultVoiceGateway(message.getGuild().gateway!!, kord.resources.httpClient, options)

        gateway.connect()

    playerManager.loadItem("https://www.youtube.com/watch?v=dQw4w9WgXcQ", object : AudioLoadResultHandler {
        override fun trackLoaded(track: AudioTrack?) {
            TODO("Not yet implemented")
        }

        override fun playlistLoaded(playlist: AudioPlaylist?) {
            TODO("Not yet implemented")
        }

        override fun noMatches() {
            TODO("Not yet implemented")
        }

        override fun loadFailed(exception: FriendlyException?) {
            TODO("Not yet implemented")
        }

    })
}


