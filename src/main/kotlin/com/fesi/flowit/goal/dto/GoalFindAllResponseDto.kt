package com.fesi.flowit.goal.dto

import com.fesi.flowit.common.util.REGEX_RGB_CODE
import com.querydsl.core.annotations.QueryProjection
import io.swagger.v3.oas.annotations.media.Schema

class GoalFindAllResponseDto @QueryProjection constructor(
    @field:Schema(
        description = "목표 ID",
        example = "1",
    )
    var goalId: Long,

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
    )
    var color: String,

    @field:Schema(
        description = "고정 여부",
        example = "true | false",
    )
    var isPinned: Boolean
)