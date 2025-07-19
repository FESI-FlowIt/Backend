package com.fesi.flowit.auth.web.response

class RegenerateResponse(
    val accessToken: String
) {
    companion object {
        fun of(accessToken: String): RegenerateResponse {
            return RegenerateResponse(accessToken)
        }
    }
}
