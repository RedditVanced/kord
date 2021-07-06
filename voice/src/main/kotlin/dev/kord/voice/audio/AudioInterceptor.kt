package dev.kord.voice.audio

import dev.kord.voice.AudioFrame
import dev.kord.voice.AudioProvider
import dev.kord.voice.SILENCE_BYTES
import dev.kord.voice.VoiceOpCode
import dev.kord.voice.command.VoiceSpeakingCommand
import dev.kord.voice.gateway.DefaultVoiceGateway
import dev.kord.voice.gateway.VoiceGateway
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import mu.KotlinLogging

private val LOG = KotlinLogging.logger { }

class AudioInterceptor(private val voiceGateway: DefaultVoiceGateway, private val audioProvider: AudioProvider) {

    private var speaking = false

    suspend fun start() {
        for (frame in audioProvider.frames) {
            LOG.debug { "Got frame $frame" }
            when (frame) {
                is AudioFrame.Silence -> voiceGateway.sendEncryptedVoice(frame.data)
                is AudioFrame.Frame -> {
                    LOG.debug { "Sending frame to gateway $frame" }
                    if(!speaking) {
                        voiceGateway.send(VoiceSpeakingCommand(5, 20, voiceGateway.ssrc!!))
                        speaking = true
                    }
                    voiceGateway.sendEncryptedVoice(frame.data)
                }
                else -> Unit
            }
        }
    }
}