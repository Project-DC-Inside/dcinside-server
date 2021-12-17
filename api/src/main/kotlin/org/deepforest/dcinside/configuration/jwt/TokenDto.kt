package org.deepforest.dcinside.configuration.jwt

data class TokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long
)