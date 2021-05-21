package json

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.orEmpty
import json.test_cases.GROUP_SUB_COMMAND
import json.test_cases.ROOT_COMMAND
import json.test_cases.SLASH_COMMAND_PERMISSIONS_UPDATE
import json.test_cases.SUB_COMMAND
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(KordPreview::class)
class InteractionTest {

    val json = Json {
        ignoreUnknownKeys = true
    }

    @JsName("groupCommand")
    @Test
    fun `group command can be deserialized`() {
        val text = GROUP_SUB_COMMAND

        val interaction = json.decodeFromString(DiscordInteraction.serializer(), text)
        with(interaction) {
            channelId shouldBe "587324906702766226"
            applicationId shouldBe "297153970613387264"
            id shouldBe "793442788670832640"
            version shouldBe 1
            type.type shouldBe 2
            token shouldBe "hunter2"
            data.name shouldBe "testsubcommands"
            data.id shouldBe "792107855418490901"
            val group = data.options.orEmpty().first()
            assertTrue(group is CommandGroup)
            group.name shouldBe "group"
            val subCommand = group.options.orEmpty().first()
            subCommand.name shouldBe "groupsubcommand"
            val arg = subCommand.options.orEmpty().first()
            arg.name shouldBe "testint"
            arg.value.int() shouldBe 1
            arg.value.string() shouldBe "1"
        }
    }

    @JsName("subCommand")
    @Test
    fun `subcommand  can be deserialized`() {
        val text = SUB_COMMAND

        val interaction = json.decodeFromString(DiscordInteraction.serializer(), text)
        with(interaction) {
            channelId shouldBe "587324906702766226"
            applicationId shouldBe "297153970613387264"
            id shouldBe "793442788670832640"
            version shouldBe 1
            type.type shouldBe 2
            token shouldBe "hunter2"
            data.name shouldBe "testsubcommands"
            data.id shouldBe "792107855418490901"
            val subCommand = data.options.orEmpty().first()
            assertTrue(subCommand is SubCommand)
            subCommand.name shouldBe "subcommand"
            val arg = subCommand.options.orEmpty().first()
            arg.name shouldBe "testint"
            arg.value.int() shouldBe 1
            arg.value.string() shouldBe "1"
        }
    }

    @JsName("rootCommand")
    @Test
    fun `root  can be deserialized`() {
        val text = ROOT_COMMAND

        val interaction = json.decodeFromString(DiscordInteraction.serializer(), text)
        with(interaction) {
            channelId shouldBe "587324906702766226"
            applicationId shouldBe "297153970613387264"
            id shouldBe "793442788670832640"
            version shouldBe 1
            type.type shouldBe 2
            token shouldBe "hunter2"
            data.name shouldBe "testsubcommands"
            data.id shouldBe "792107855418490901"
            val arg = data.options.orEmpty().first()
            assertTrue(arg is CommandArgument)
            arg.name shouldBe "testint"
            arg.value.int() shouldBe 1
            arg.value.string() shouldBe "1"
        }
    }

    @JsName("slashCommand")
    @Test
    fun `slash command permissions can be serialized`() {
        val text = SLASH_COMMAND_PERMISSIONS_UPDATE

        val interaction = json.decodeFromString(DiscordGuildApplicationCommandPermissions.serializer(), text)

        with(interaction) {
            id shouldBe "833008574669651989"
            applicationId shouldBe "535129406650318860"
            guildId shouldBe "809471441719787602"

            with(permissions.first()) {
                id shouldBe "827126703301066792"
                type shouldBe DiscordGuildApplicationCommandPermission.Type.Role
                permission shouldBe true
            }
        }
    }
}