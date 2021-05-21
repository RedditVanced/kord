package dev.kord.common.entity.optional

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class OptionalTest {

    @JsName("valueOnNullable")
    @Test
    fun `creating optional from nullable value returns Value on non-null value`() {
        val value: Int? = 5
        val optional = Optional(value)

        assertTrue(optional is Optional.Value)
        assertEquals(optional.value, value)
    }

    @JsName("nullOnNullable")
    @Test
    fun `creating optional from nullable value returns Null on null value`() {
        val value: Int? = null
        val optional = Optional(value)

        assertTrue(optional is Optional.Null)
    }


    @Serializable
    private class NullOptionalEntity(val value: Optional<String?>)

    @JsName("deserializeNull")
    @Test
    fun `deserializing null in nullable optional assigns Null`() {
        //language=json
        val json = """{ "value":null }"""

        val entity = Json.decodeFromString<NullOptionalEntity>(json)

        assertTrue(entity.value is Optional.Null)
    }


    @Serializable
    class EmptyOptionalEntity(val value: Optional<String?> = Optional.Missing())

    @JsName("deserializeNothing")
    @Test
    fun `deserializing nothing in nullable optional assigns Missing`() {
        //language=json
        val json = """{}"""

        val entity = Json.decodeFromString<EmptyOptionalEntity>(json)

        assertTrue(entity.value is Optional.Missing)
    }


    @Serializable
    class UnexpectedEmptyOptionalEntity(val value: Optional<String> = Optional.Missing())

    @JsName("deserializeNullOnNulNullable")
    @Test
    fun `deserializing nothing in non-nullable optional assigns Missing`() {
        //language=json
        val json = """{}"""

        val entity = Json.decodeFromString<UnexpectedEmptyOptionalEntity>(json)

        assertTrue(entity.value is Optional.Missing)
    }


    @Serializable
    private class UnexpectedNullOptionalEntity(@Suppress("unused") val value: Optional<String> = Optional.Missing())

    @JsName("deserializeNullOnNonNullable")
    @Test
    fun `deserializing null in non-nullable optional throws SerializationException`() {
        //language=json
        val json = """{ "value":null }"""

        assertFailsWith<SerializationException> {
            Json.decodeFromString<UnexpectedNullOptionalEntity>(json)
        }
    }

}