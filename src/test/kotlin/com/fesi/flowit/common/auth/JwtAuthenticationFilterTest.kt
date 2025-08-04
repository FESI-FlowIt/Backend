import com.fasterxml.jackson.databind.ObjectMapper
import com.fesi.flowit.common.response.exceptions.FailToParseJwtException
import com.fesi.flowit.common.auth.JwtAuthenticationFilter
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.common.auth.dto.TokenInfo
import com.fesi.flowit.common.auth.dto.valid
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class JwtAuthenticationFilterTest : StringSpec({

    lateinit var jwtProcessor: JwtProcessor
    lateinit var objectMapper: ObjectMapper
    lateinit var filter: JwtAuthenticationFilter
    lateinit var request: MockHttpServletRequest
    lateinit var response: MockHttpServletResponse
    lateinit var chain: FilterChain

    beforeEach {
        jwtProcessor = mockk<JwtProcessor>()
        objectMapper = mockk<ObjectMapper>(relaxed = true)
        filter = JwtAuthenticationFilter(jwtProcessor, objectMapper)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        chain = mockk<FilterChain>(relaxed = true)
    }

    afterEach {
        SecurityContextHolder.clearContext()
    }

    "Authorization 헤더가 없는 요청은 jwt 필터에서 처리하지 않는다" {

        filter.doFilterInternal(mockk<HttpServletRequest>() {
            every<String?> { getHeader("Authorization") } returns null
        }, response, chain)

        verify(exactly = 0) { jwtProcessor.handle(any()) }
    }

    "Authorization 헤더가 Bearer로 시작하지 않는 경우 jwt 필터에서 처리하지 않는다" {
        request.addHeader("Authorization", "Basic sometoken")

        filter.doFilterInternal(request, response, chain)

        verify(exactly = 0) { jwtProcessor.handle(any()) }
    }

    "유효한 jwt 토큰인 경우 jwt 필터에서 처리한다" {
        val validToken = "valid.jwt.token"
        request.addHeader("Authorization", "Bearer $validToken")

        every { jwtProcessor.handle(validToken) } returns TokenInfo.valid()
        every { jwtProcessor.getAuthentication(any()) } returns mockk()

        filter.doFilterInternal(request, response, chain)

        verify { jwtProcessor.handle(validToken) }
        verify { chain.doFilter(request, response) }
    }

    "유효하지 않은 jwt 토큰인 경우 이후 필터로 넘기지 않는다" {
        val invalidToken = "invalid.jwt.token"
        request.addHeader("Authorization", "Bearer $invalidToken")

        val exception = FailToParseJwtException()
        every { jwtProcessor.handle(invalidToken) } throws exception

        filter.doFilterInternal(request, response, chain)

        verify { jwtProcessor.handle(invalidToken) }
        verify(exactly = 0) { chain.doFilter(any(), any()) }
    }

    "유효하지 않은 jwt 토큰인 경우 해당 상황을 설명하는 응답을 반환한다" {
        val invalidUserToken = "invalid.user.token"
        request.addHeader("Authorization", "Bearer $invalidUserToken")

        val exception = FailToParseJwtException()
        every { jwtProcessor.handle(invalidUserToken) } throws exception

        filter.doFilterInternal(request, response, chain)

        response.status shouldBe HttpServletResponse.SC_UNAUTHORIZED
    }
})