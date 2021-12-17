package org.deepforest.dcinside.auth.dto

data class TokenReqDto(
    val accessToken: String,
    val refreshToken: String
)