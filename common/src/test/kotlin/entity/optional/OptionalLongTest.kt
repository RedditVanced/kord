package dev.kord.common.entity.optional

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class OptionalLongTest {

    @Test
    fun `deserializing nothing in optional assigns Missing`() {
        @Language("json")
        val json = """{}"""

        @Serializable
        class Entity(val value: OptionalLong = OptionalLong.Missing)

        val entity = Json.decodeFromString<Entity>(json)

        assert(entity.value is OptionalLong.Missing)
    }

    @Test
    fun `deserializing null in optional throws SerializationException`() {
        @Language("json")
        val json = """{ "value":null }"""

        @Serializable
        class Entity(@Suppress("unused") val value: OptionalLong = OptionalLong.Missing)

        org.junit.jupiter.api.assertThrows<SerializationException> {
            Json.decodeFromString<Entity>(json)
        }
    }

    @Test
    fun `deserializing value in optional assigns Value`() {
        @Language("json")
        val json = """{ "value":5 }"""

        @Serializable
        class Entity(@Suppress("unused") val value: OptionalLong = OptionalLong.Missing)

        val entity = Json.decodeFromString<Entity>(json)
        require(entity.value is OptionalLong.Value)

        Assertions.assertEquals(5, entity.value.value)
    }

}
