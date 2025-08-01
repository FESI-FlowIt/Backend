package com.fesi.flowit.common.util

const val REGEX_RGB_CODE = "^#([A-Fa-f0-9]{6})\$"

fun String.isRGBColor(): Boolean {
    return this.matches(REGEX_RGB_CODE.toRegex())
}
