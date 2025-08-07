package com.fesi.flowit.e2e

import com.fesi.flowit.auth.local.web.request.SignInRequest
import com.fesi.flowit.auth.local.web.response.SignInResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.goal.dto.GoalCreateRequestDto
import com.fesi.flowit.goal.dto.GoalInfoResponseDto
import com.fesi.flowit.note.dto.NoteCreateRequestDto
import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.todo.dto.TodoCreateRequestDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.user.web.request.UserRequest
import com.fesi.flowit.user.web.response.UserResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiE2ETest(
    @Autowired private val restTemplate: TestRestTemplate
) : BehaviorSpec({

    lateinit var accessToken: String
    var goalId: Long? = null

    beforeSpec {
        // 1. 회원가입
        val signUpRequest = UserRequest(
            email = "y@gmail.com",
            name = "gsh",
            password = "blahblah"
        )
        val signUpResponse = restTemplate.exchange(
            "/users",
            HttpMethod.POST,
            HttpEntity(signUpRequest),
            object : ParameterizedTypeReference<ApiResult.Success<UserResponse>>() {}
        )
        print(signUpResponse.body!!.result)

        // 2. 로그인
        val signInRequest = SignInRequest(
            email = "y@gmail.com",
            password = "blahblah"
        )
        val signInResponse = restTemplate.exchange(
            "/auths/signIn",
            HttpMethod.POST,
            HttpEntity(signInRequest),
            object : ParameterizedTypeReference<ApiResult.Success<SignInResponse>>() {}
        )

        accessToken = signInResponse.body!!.result.accessToken
    }

    Given("발급받은 토큰으로 목표를 생성하면") {
        val response = createNewGoal(accessToken, restTemplate)

        goalId = response.body!!.result.goalId

        Then("201 CREATED와 함께 목표 ID가 반환된다") {
            response.statusCode shouldBe HttpStatus.CREATED
            goalId!!.compareTo(0) shouldBeGreaterThan 0
        }
    }

    Given("발급받은 토큰으로 todo를 생성하면") {
        val goalId = createNewGoal(accessToken, restTemplate).body!!.result.goalId

        val response = createNewTodo(accessToken, restTemplate, goalId)

        Then("201 CREATED가 응답된다") {
            response.statusCode shouldBe HttpStatus.CREATED
        }
    }

    Given("발급받은 토큰으로 note를 생성하면") {
        val goalId = createNewGoal(accessToken, restTemplate).body!!.result.goalId

        val todoId = createNewTodo(accessToken, restTemplate, goalId).body!!.result.todoId

        val response = createNewNote(accessToken, restTemplate, todoId)

        Then("201 CREATED가 응답된다") {
            response.statusCode shouldBe HttpStatus.CREATED
        }
    }
})

fun createNewNote(
    accessToken: String,
    restTemplate: TestRestTemplate,
    todoId: Long
): ResponseEntity<ApiResult.Created<NoteInfoResponseDto>> {
    val noteRequest = NoteCreateRequestDto(
        title = "제목",
        link = "",
        content = "내용",
        wordCount = 1
    )
    val headers = HttpHeaders().apply { setBearerAuth(accessToken) }

    val response = restTemplate.exchange(
        "/todos/${todoId}/notes",
        HttpMethod.POST,
        HttpEntity(noteRequest, headers),
        object : ParameterizedTypeReference<ApiResult.Created<NoteInfoResponseDto>>() {}
    )

    return response
}

fun createNewGoal(
    accessToken: String,
    restTemplate: TestRestTemplate
): ResponseEntity<ApiResult.Created<GoalInfoResponseDto>> {
    val goalRequest = GoalCreateRequestDto(
        name = "테스트 목표",
        color = "#000000",
        dueDateTime = "2025-08-19".toLocalDateTime()
    )
    val headers = HttpHeaders().apply { setBearerAuth(accessToken) }

    val response = restTemplate.exchange(
        "/goals",
        HttpMethod.POST,
        HttpEntity(goalRequest, headers),
        object : ParameterizedTypeReference<ApiResult.Created<GoalInfoResponseDto>>() {}
    )

    return response
}

fun createNewTodo(
    accessToken: String,
    restTemplate: TestRestTemplate,
    goalId: Long
): ResponseEntity<ApiResult.Created<TodoCreateResponseDto>> {
    val todoRequest = TodoCreateRequestDto(
        name = "테스트 할 일",
        goalId = goalId
    )
    val headers = HttpHeaders().apply { setBearerAuth(accessToken) }

    val response = restTemplate.exchange(
        "/todos",
        HttpMethod.POST,
        HttpEntity(todoRequest, headers),
        object : ParameterizedTypeReference<ApiResult.Created<TodoCreateResponseDto>>() {}
    )
    return response
}

private fun String.toLocalDateTime() =
    LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay()
