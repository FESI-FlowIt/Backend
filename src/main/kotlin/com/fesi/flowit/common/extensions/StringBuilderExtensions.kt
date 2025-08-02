package com.fesi.flowit.common.extensions

/**
 * 마지막 문자가 ',' 이면 제거
 */
fun StringBuilder.removeLastComma() {
    if (this.isNotEmpty() && this.last() == ',') {
        this.deleteCharAt(this.length - 1)
    }
}