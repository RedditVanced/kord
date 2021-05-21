@file:Suppress("EXPERIMENTAL_API_USAGE")

package json

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import json.test_cases.CROSS_POSTED
import json.test_cases.MESSAGE
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames
import kotlin.js.JsName
import kotlin.test.Test

class MessageTest {

    @JsName("message")
    @Test
    fun `Message serialization`() {
        val message: DiscordMessage = Json.decodeFromString(DiscordMessage.serializer(), MESSAGE)

        with(message) {
            reactions.value!!.size shouldBe 1
            with(reactions.value!!.first()) {
                count shouldBe 1
                me shouldBe false
                with(emoji) {
                    id shouldBe null
                    name shouldBe "🔥"
                }
            }
            attachments shouldBe emptyList()
            tts shouldBe false
            embeds shouldBe emptyList()
            timestamp shouldBe "2017-07-11T17:27:07.299000+00:00"
            mentionEveryone shouldBe false
            id.asString shouldBe "334385199974967042"
            pinned shouldBe false
            editedTimestamp shouldBe null
            with(author) {
                username shouldBe "Mason"
                discriminator shouldBe "9999"
                id shouldBe "53908099506183680"
                avatar shouldBe "a_bab14f271d565501444b2ca3be944b25"
            }
            mentionRoles shouldBe emptyList()
            content shouldBe "Supa Hot"
            channelId shouldBe "290926798999357250"
            mentions shouldBe emptyList()
            type.code shouldBe 0
        }

    }
}

@JsName("user")
@Test
fun `User serialization`() {
    val message = Json.decodeFromString(DiscordMessage.serializer(), CROSS_POSTED)

    with(message) {
        reactions.value!!.size shouldBe 1
        with(reactions.value!!.first()) {
            count shouldBe 1
            me shouldBe false
            with(emoji) {
                id shouldBe null
                name shouldBe "🔥"
            }
        }
        attachments shouldBe emptyList()
        tts shouldBe false
        embeds shouldBe emptyList()
        timestamp shouldBe "2017-07-11T17:27:07.299000+00:00"
        mentionEveryone shouldBe false
        id.asString shouldBe "334385199974967042"
        pinned shouldBe false
        editedTimestamp shouldBe null
        with(author) {
            username shouldBe "Mason"
            discriminator shouldBe "9999"
            id.asString shouldBe "53908099506183680"
            avatar shouldBe "a_bab14f271d565501444b2ca3be944b25"
        }
        mentionRoles shouldBe emptyList()
        content shouldBe "Big news! In this <#278325129692446722> channel!"
        channelId.asString shouldBe "290926798999357250"
        mentions shouldBe emptyList()
        type.code shouldBe 0
        flags shouldBe MessageFlags(MessageFlag.IsCrossPost.code)
        with(messageReference.value!!) {
            channelId.value?.asString shouldBe "278325129692446722"
            guildId.value!!.asString shouldBe "278325129692446720"
            id.value!!.asString shouldBe "306588351130107906"
        }
    }

}
