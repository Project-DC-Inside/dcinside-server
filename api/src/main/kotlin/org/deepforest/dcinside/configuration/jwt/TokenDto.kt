package org.deepforest.dcinside.configuration.jwt

data class TokenDto(
    private val grantType: String,
    private val accessToken: String,
    private val refreshToken: String,
    private val accessTokenExpiresIn: Long
)