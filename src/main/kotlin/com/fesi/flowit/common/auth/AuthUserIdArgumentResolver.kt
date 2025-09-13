package com.fesi.flowit.common.auth

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthUserIdArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(methodParam: MethodParameter): Boolean {
        val boxedLong = Long::class.javaObjectType
        val primitiveLong = java.lang.Long.TYPE

        val paramAnnotation = methodParam.getParameterAnnotation(AuthUserId::class.java)
        val paramType = methodParam.parameterType
        return paramAnnotation != null && (paramType == boxedLong || paramType == primitiveLong)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Long {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated) {
            throw AuthException.fromCodeWithMsg(
                code = ApiResultCode.UNAUTHORIZED,
                msg = "Request requires authentication, but fail to find user id"
            )
        }

        val principal = authentication.principal
        if (principal is User) {
            return principal.id
        } else {
            throw AuthException.fromCodeWithMsg(
                code = ApiResultCode.UNAUTHORIZED,
                msg = "Request requires authentication, but given authentication is not applicable"
            )
        }
    }
}
