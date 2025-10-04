package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.GoalException
import com.fesi.flowit.goal.dto.GoalCreateRequestDto
import com.fesi.flowit.goal.dto.GoalInfoResponseDto
import com.fesi.flowit.goal.service.GoalService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class GoalControllerImplTest : StringSpec({
    val goalService = mockk<GoalService>(relaxed = true)
    val controller = GoalControllerImpl(goalService)

    "목표 생성 API는 유효한 요청으로 목표를 생성할 수 있다" {
        // given
        val userId = 1L
        val request = GoalCreateRequestDto(
            name = "건강 프로젝트",
            color = "#00FF00",
            dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
        )

        val expectedResponse = GoalInfoResponseDto(
            goalId = 100L,
            name = "건강 프로젝트",
            color = "#00FF00",
            createdDateTime = LocalDateTime.now(),
            dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0),
            modifiedDateTime = LocalDateTime.now(),
            isPinned = false
        )

        every {
            goalService.createGoal(
                userId = userId,
                name = request.name,
                color = request.color,
                dueDateTime = request.dueDateTime
            )
        } returns expectedResponse

        // when
        val response = controller.createGoal(request, userId)

        // then
        response.statusCode shouldBe HttpStatus.CREATED
        verify {
            goalService.createGoal(
                userId = userId,
                name = "건강 프로젝트",
                color = "#00FF00",
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
            )
        }
    }

    "목표 생성 API는 이름이 비어있으면 예외를 던진다" {
        // given & when & then
        shouldThrow<GoalException> {
            GoalCreateRequestDto(
                name = "",
                color = "#00FF00",
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
            )
        }
    }

    "목표 생성 API는 이름이 30자를 초과하면 예외를 던진다" {
        // given
        val longName = "a".repeat(31)

        // when & then
        shouldThrow<GoalException> {
            GoalCreateRequestDto(
                name = longName,
                color = "#00FF00",
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
            )
        }
    }

    "목표 생성 API는 유효하지 않은 색상 코드로 예외를 던진다" {
        // given & when & then
        shouldThrow<GoalException> {
            GoalCreateRequestDto(
                name = "건강 프로젝트",
                color = "invalid-color",
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
            )
        }
    }

    "목표 생성 API는 과거 날짜를 마감일로 설정할 수 있다" {
        // given
        val userId = 1L
        val pastDate = LocalDateTime.now().minusDays(1)
        val request = GoalCreateRequestDto(
            name = "과거 목표",
            color = "#FF0000",
            dueDateTime = pastDate
        )

        val expectedResponse = GoalInfoResponseDto(
            goalId = 101L,
            name = "과거 목표",
            color = "#FF0000",
            createdDateTime = LocalDateTime.now(),
            dueDateTime = pastDate,
            modifiedDateTime = LocalDateTime.now(),
            isPinned = false
        )

        every {
            goalService.createGoal(
                userId = userId,
                name = request.name,
                color = request.color,
                dueDateTime = request.dueDateTime
            )
        } returns expectedResponse

        // when
        val response = controller.createGoal(request, userId)

        // then
        response.statusCode shouldBe HttpStatus.CREATED
        verify {
            goalService.createGoal(
                userId = userId,
                name = "과거 목표",
                color = "#FF0000",
                dueDateTime = pastDate
            )
        }
    }

    "목표 생성 API는 다양한 색상 형식을 허용한다" {
        val validColors = listOf("#000000", "#FFFFFF", "#ff0000", "#00FF00", "#0000FF")

        validColors.forEach { color ->
            // given
            val userId = 1L
            val request = GoalCreateRequestDto(
                name = "색상 테스트",
                color = color,
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
            )

            val expectedResponse = GoalInfoResponseDto(
                goalId = 102L,
                name = "색상 테스트",
                color = color,
                createdDateTime = LocalDateTime.now(),
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0),
                modifiedDateTime = LocalDateTime.now(),
                isPinned = false
            )

            every {
                goalService.createGoal(
                    userId = userId,
                    name = request.name,
                    color = request.color,
                    dueDateTime = request.dueDateTime
                )
            } returns expectedResponse

            // when
            val response = controller.createGoal(request, userId)

            // then
            response.statusCode shouldBe HttpStatus.CREATED
        }
    }

    "목표 생성 API는 유효한 이름 길이(1-30자)를 허용한다" {
        val validNames = listOf(
            "a",           // 1자
            "목표 이름",     // 중간 길이
            "a".repeat(30) // 30자
        )

        validNames.forEach { name ->
            // given
            val userId = 1L
            val request = GoalCreateRequestDto(
                name = name,
                color = "#FF0000",
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
            )

            val expectedResponse = GoalInfoResponseDto(
                goalId = 103L,
                name = name,
                color = "#FF0000",
                createdDateTime = LocalDateTime.now(),
                dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0),
                modifiedDateTime = LocalDateTime.now(),
                isPinned = false
            )

            every {
                goalService.createGoal(
                    userId = userId,
                    name = request.name,
                    color = request.color,
                    dueDateTime = request.dueDateTime
                )
            } returns expectedResponse

            // when
            val response = controller.createGoal(request, userId)

            // then
            response.statusCode shouldBe HttpStatus.CREATED
        }
    }

    "목표 생성 API는 서비스 레이어에서 발생한 예외를 그대로 전파한다" {
        // given
        val userId = 1L
        val request = GoalCreateRequestDto(
            name = "건강 프로젝트",
            color = "#00FF00",
            dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
        )

        every {
            goalService.createGoal(
                userId = userId,
                name = request.name,
                color = request.color,
                dueDateTime = request.dueDateTime
            )
        } throws GoalException.fromCode(ApiResultCode.GOAL_ID_INVALID)

        // when & then
        shouldThrow<GoalException> {
            controller.createGoal(request, userId)
        }
    }
})