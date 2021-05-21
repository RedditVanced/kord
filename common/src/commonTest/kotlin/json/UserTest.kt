@file:Suppress("EXPERIMENTAL_API_USAGE")

package json

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.UserFlags
import json.test_cases.USER
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import kotlin.test.Test

class UserTest {

    @JsName("user")
    @Test
    fun `User serialization`() {
        val user = Json.decodeFromString(DiscordUser.serializer(), USER)

        with(user) {
            id.asString shouldBe "80351110224678912"
            username shouldBe "Nelly"
            discriminator shouldBe "1337"
            avatar shouldBe "8342729096ea3675442027381ff50dfe"
            verified.asNullable!! shouldBe true
            email.value shouldBe "nelly@discordapp.com"
            flags.value shouldBe UserFlags(64)
            premiumType.value!!.value shouldBe 1
        }

    }
}

