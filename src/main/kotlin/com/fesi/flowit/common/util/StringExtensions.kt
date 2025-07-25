package com.fesi.flowit.common.util

fun String.extractAccessToken(): String {
    val bearerPrefix = "Bearer "
    return this.removePrefix(bearerPrefix).trim()
}