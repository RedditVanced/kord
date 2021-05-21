@file:Suppress("EXPERIMENTAL_API_USAGE")

package json

import dev.kord.common.entity.DiscordEmoji
import json.test_cases.CUSTOM_EMOJI
import json.test_cases.EMOJI
import json.test_cases.STANDARD_EMOJI
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import kotlin.test.Test


class EmojiTest {

    @JsName("emoji")
    @Test
    fun `Emoji serialization`() {
        val emoji = Json.decodeFromString(DiscordEmoji.serializer(), CUSTOM_EMOJI)

        with(emoji) {
            id!!.asString shouldBe "41771983429993937"
            name shouldBe "LUL"
        }

    }
}

@JsName("standardEmoji")
@Test
fun `Standard Emoji serialization`() {
    val emoji = Json.decodeFromString(DiscordEmoji.serializer(), STANDARD_EMOJI)

    with(emoji) {
        id shouldBe null
        name shouldBe "🔥"
    }

}

@JsName("emoji")
@Test
fun `Emoji serialization`() {
    val emoji = Json.decodeFromString(DiscordEmoji.serializer(), EMOJI)

    with(emoji) {
        id shouldBe "41771983429993937"
        name shouldBe "LUL"
        roles shouldBe listOf("41771983429993000", "41771983429993111")
        with(user.value!!) {
            username shouldBe "Luigi"
            discriminator shouldBe "0002"
            id shouldBe "96008815106887111"
            avatar shouldBe "5500909a3274e1812beb4e8de6631111"
        }
        requireColons shouldBe true
        managed shouldBe false
        animated shouldBe false
    }

}


