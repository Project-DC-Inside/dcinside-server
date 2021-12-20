package org.deepforest.dcinside.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.auth.dto.SigninReqDto
import org.deepforest.dcinside.auth.dto.SignupReqDto
import org.deepforest.dcinside.auth.dto.TokenReqDto
import org.deepforest.dcinside.configuration.jwt.TokenDto
import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "/auth", description = "인증 API")
@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {
    @Operation(
        summary = "회원가입", description = "Request: SignupReqDto, Response: String",
        responses = [
            ApiResponse(responseCode = "200", description = "가입 성공 후 username 반환")
        ]
    )
    @PostMapping("/signup")
    fun signup(
        @RequestBody signupReqDto: SignupReqDto
    ): ResponseDto<String> = ResponseDto.ok(
        authService.signup(signupReqDto)
    )

    @Operation(
        summary = "로그인",
        description = "Request: SigninReqDto, Response: TokenDto",
        responses = [
            ApiResponse(responseCode = "200", description = "로그인 성공 후 Access Token 발급")
        ]
    )
    @PostMapping("/signin")
    fun signin(
        @RequestBody signinReqDto: SigninReqDto
    ): ResponseDto<TokenDto> = ResponseDto.ok(
        authService.signin(signinReqDto)
    )

    @Operation(
        summary = "토큰 재발급",
        description = "Request: TokenReqDto, Response: TokenDto",
        responses = [
            ApiResponse(responseCode = "200", description = "RefreshToken 인증 성공 후 새로운 Access Token 발급")
        ]
    )
    @PostMapping("/reissue")
    fun reissue(
        @RequestBody tokenReqDto: TokenReqDto
    ): ResponseDto<TokenDto> = ResponseDto.ok(
        authService.reissue(tokenReqDto)
    )
}
