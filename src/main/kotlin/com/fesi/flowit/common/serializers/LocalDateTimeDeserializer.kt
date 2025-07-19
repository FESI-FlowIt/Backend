package com.fesi.flowit.common.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * 역직렬화 시 LocalDateTime Format으로 변환
 */
class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDateTime {
        val text = p.text

        return try {
            LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: DateTimeParseException) {
            parseUsingLocalDate(text)
        } catch (e: Exception) {
            throw IllegalArgumentException("Not supported data type")
        }
    }

    private fun parseUsingLocalDate(text: String): LocalDateTime {
        return try {
            val date = LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE)
            return date.atStartOfDay()
        } catch (e: Exception) {
            throw IllegalArgumentException("Not supported data type")
        }
    }
}