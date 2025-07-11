import com.fesi.flowit.common.auth.JwtAuthenticationFilter
import com.fesi.flowit.common.auth.JwtProcessor
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class JwtAuthenticationFilterTest : StringSpec({
    val jwtProcessor = mockk<JwtProcessor>()
    val filter = JwtAuthenticationFilter(jwtProcessor)

    "Authorization 헤더가 없는 요청은 jwt 필터에서 처리하지 않는다" {
        val request = mockk<HttpServletRequest>() {
            every { getHeader("Authorization") } returns null
        }
        val response = mockk<HttpServletResponse>()
        val chain = mockk<FilterChain>(relaxed = true)

        filter.doFilterInternal(request, response, chain)

        verify(exactly = 0) { jwtProcessor.x(any()) }
    }
})
