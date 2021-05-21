@file:Suppress("EXPERIMENTAL_API_USAGE")

package json

import dev.kord.common.entity.DiscordVoiceState
import json.test_cases.VOICE_STATE
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import kotlin.test.Ignore
import kotlin.test.Test

class VoiceStateTest {

    @JsName("voiceState")
    @Test
    @Ignore // "official documentation example is incorrect"
    fun `VoiceState serialization`() {
        val state = Json.decodeFromString(DiscordVoiceState.serializer(), VOICE_STATE)

        with(state) {
            channelId!!.asString shouldBe "157733188964188161"
            userId.asString shouldBe "80351110224678912"
            sessionId shouldBe "90326bd25d71d39b9ef95b299e3872ff"
            deaf shouldBe false
            mute shouldBe false
            selfDeaf shouldBe false
            selfMute shouldBe true
            suppress shouldBe false
        }

    }
}
