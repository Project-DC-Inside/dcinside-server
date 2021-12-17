package org.deepforest.dcinside.auth

import org.deepforest.dcinside.auth.dto.SigninReqDto
import org.deepforest.dcinside.auth.dto.SignupReqDto
import org.deepforest.dcinside.auth.dto.TokenReqDto
import org.deepforest.dcinside.configuration.jwt.TokenDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signup")
    fun signup(
        @RequestBody signupReqDto: SignupReqDto
    ): ResponseEntity<String> = ResponseEntity.ok(
        authService.signup(signupReqDto)
    )

    @PostMapping("/signin")
    fun signin(
        @RequestBody signinReqDto: SigninReqDto
    ): ResponseEntity<TokenDto> = ResponseEntity.ok(
        authService.signin(signinReqDto)
    )

    @PostMapping("/reissue")
    fun reissue(
        @RequestBody tokenReqDto: TokenReqDto
    ): ResponseEntity<TokenDto> = ResponseEntity.ok(
        authService.reissue(tokenReqDto)
    )
}
