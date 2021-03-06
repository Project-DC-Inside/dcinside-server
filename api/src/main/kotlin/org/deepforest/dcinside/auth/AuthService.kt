package org.deepforest.dcinside.auth

import org.deepforest.dcinside.auth.dto.SigninReqDto
import org.deepforest.dcinside.auth.dto.SignupReqDto
import org.deepforest.dcinside.auth.dto.TokenReqDto
import org.deepforest.dcinside.configuration.jwt.TokenDto
import org.deepforest.dcinside.configuration.jwt.TokenProvider
import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.entity.auth.RefreshToken
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.exception.ApiException
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.member.hasNotUsername
import org.deepforest.dcinside.validation.check
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class AuthService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Transactional
    fun signup(signupReqDto: SignupReqDto) {
        val member: Member = signupReqDto.toMember(passwordEncoder)

        check(memberRepository.hasNotUsername(member.username)) { ErrorCode.CONFLICT_USERNAME }

        memberRepository.save(member)
    }

    @Transactional
    fun signin(signinReqDto: SigninReqDto): TokenDto {
        // 1. username, password 를 기반으로 AuthenticationToken 생성
        val authenticationToken = signinReqDto.toAuthenticationToken()

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        val authentication: Authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        val tokenDto = tokenProvider.generateTokenDto(authentication)

        // 4. RefreshToken 저장
        RefreshToken(authentication.name, tokenDto.refreshToken).also {
            refreshTokenRepository.save(it)
        }

        // 5. 토큰 발급
        return tokenDto
    }

    @Transactional
    fun reissue(tokenReqDto: TokenReqDto): TokenDto {
        // 1. Refresh Token 검증
        check(tokenProvider.validateToken(tokenReqDto.refreshToken)) { ErrorCode.INVALID_REFRESH_TOKEN }

        // 2. Access Token 에서 Member ID 가져오기
        val authentication = tokenProvider.getAuthentication(tokenReqDto.accessToken)

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        val refreshToken = refreshTokenRepository.findByKey(authentication.name)
            ?: throw ApiException(ErrorCode.NOT_FOUND_REFRESH_TOKEN)

        // 4. Refresh Token 일치하는지 검사
        check(refreshToken.value == tokenReqDto.refreshToken) { ErrorCode.MISMATCH_REFRESH_TOKEN }

        // 5. 새로운 토큰 생성 후 발급
        return tokenProvider.generateTokenDto(authentication).also {
            refreshToken.updateValue(it.refreshToken)
        }
    }
}