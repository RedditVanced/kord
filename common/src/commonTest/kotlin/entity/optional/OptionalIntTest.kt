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

internal class OptionalIntTest {

    @Serializable
    private class EmptyOptionalEntity(val value: OptionalInt = OptionalInt.Missing)

    @JsName("deserializeNothing")
    @Test
    fun `deserializing nothing in optional assigns Missing`(){
        //language=json
        val json = """{}"""

        val entity = Json.decodeFromString<EmptyOptionalEntity>(json)

        assertTrue(entity.value is OptionalInt.Missing)
    }


    @Serializable
    private class NullOptionalEntity(@Suppress("unused") val value: OptionalInt = OptionalInt.Missing)

    @JsName("deserializeNull")
    @Test
    fun `deserializing null in optional throws SerializationException`(){
        //language=JSON
        val json = """{ "value":null }"""


        assertFailsWith<SerializationException> {
            Json.decodeFromString<NullOptionalEntity>(json)
        }
    }

    @Serializable
    class ValueOptionalEntity(@Suppress("unused") val value: OptionalInt = OptionalInt.Missing)

    @JsName("deserializeValue")
    @Test
    fun `deserializing value in optional assigns Value`(){
        //language=json
        val json = """{ "value":5 }"""

        val entity = Json.decodeFromString<ValueOptionalEntity>(json)
        require(entity.value is OptionalInt.Value)

        assertEquals(5, entity.value.value)
    }

}
