package com.fesi.flowit.goal.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.GoalException
import com.fesi.flowit.common.serializers.LocalDateTimeDeserializer
import com.fesi.flowit.common.extensions.REGEX_RGB_CODE
import com.fesi.flowit.common.extensions.isRGBColor
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GoalModifyRequestDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var userId: Long,

    @field:Schema(
        description = "목표 이름",
        example = "목표 이름",
        minLength = 1,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var name: String,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val color: String,

    @field:Schema(
        description = "마감 기한",
        example = "2025-07-19T14:29:00 | 2025-07-19",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val dueDateTime: LocalDateTime,
) {
    init {
        validateName()
        validateColor()
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw GoalException.fromCodeWithMsg(ApiResultCode.BAD_REQUEST, "Goal name is required.")
        }

        if (name.length !in 1..30) {
            throw GoalException.fromCode(ApiResultCode.GOAL_INVALID_NAME_LENGTH)
        }
    }

    private fun validateColor() {
        if (isNonRGBColor(color)) {
            throw GoalException.fromCode(ApiResultCode.RGB_FORMAT_INVALID)
        }
    }

    private fun isNonRGBColor(color: String): Boolean {
        return !color.isRGBColor()
    }
}