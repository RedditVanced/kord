package json

import dev.kord.common.DiscordBitSet
import dev.kord.common.entity.DiscordRole
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class PermissionsTest {

    @JsName("addPermission")
    @Test
    fun `adding permissions together does not swallow the universe`() {
        Permission.values.fold(Permissions(DiscordBitSet(0))) { acc, permission ->
            acc + permission
        }
    }

    @JsName("testAllPermissions")
    @Test
    fun `Permission All does not swallow the universe`() {
        Permission.All //oh yeah, this is worthy of a test
    }

    @JsName("serialize")
    @Test
    fun `permissions serialization test`() {
        val expected = buildJsonObject {
            put("id", "12323232")
            put("name", "Korduers")
            put("color", 0)
            put("hoist", true)
            put("position", 0)
            put("permissions", "123456789876543000000000000")
            put("managed", false)
            put("mentionable", false)
        }
        val actual = Json.decodeFromJsonElement(DiscordRole.serializer(), expected)
        assertEquals("123456789876543000000000000", actual.permissions.code.value)

    }

}