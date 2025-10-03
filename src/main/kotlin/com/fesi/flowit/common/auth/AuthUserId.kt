package com.fesi.flowit.common.auth

import io.swagger.v3.oas.annotations.Parameter


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Parameter(hidden = true)
@MustBeDocumented
annotation class AuthUserId