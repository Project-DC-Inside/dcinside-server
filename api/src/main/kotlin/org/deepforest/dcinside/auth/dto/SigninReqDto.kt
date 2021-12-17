package org.deepforest.dcinside.auth.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

data class SigninReqDto(
    val username: String,
    val password: String
) {
    fun toAuthenticationToken(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(username, password)
    }
}