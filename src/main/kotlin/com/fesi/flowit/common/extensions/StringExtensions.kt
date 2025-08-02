package com.fesi.flowit.common.extensions

const val REGEX_RGB_CODE = "^#([A-Fa-f0-9]{6})\$"

fun String.isRGBColor(): Boolean {
    return this.matches(REGEX_RGB_CODE.toRegex())
}

fun String.extractAccessToken(): String {
    val bearerPrefix = "Bearer "
    return this.removePrefix(bearerPrefix).trim()
}