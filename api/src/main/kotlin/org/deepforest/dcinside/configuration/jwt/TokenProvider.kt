package org.deepforest.dcinside.configuration.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import java.security.Key
import java.util.Date


@Component
class TokenProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String
) {
    private val key: Key

    init {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "bearer"
        private const val ACCESS_TOKEN_EXPIRE_TIME  = 1000 * 60 * 30 // 30분
        private const val REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7 // 7일
    }

    fun generateTokenDto(authentication: Authentication): TokenDto {
        // 권한들 가져오기
        val authorities = authentication.authorities
            .joinToString(",") { it.authority }

        val now: Long = Date().time

        // Access Token 생성
        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
        val accessToken: String = Jwts.builder()
            .setSubject(authentication.name)         // payload "sub": "name"
            .claim(AUTHORITIES_KEY, authorities)     // payload "auth": "ROLE_USER"
            .setExpiration(accessTokenExpiresIn)     // payload "exp": 1516239022 (예시)
            .signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
            .compact()

        // Refresh Token 생성
        val refreshToken: String = Jwts.builder()
            .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return TokenDto(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time
        )
    }

    fun getAuthentication(accessToken: String): Authentication {
        // 토큰 복호화
        val claims: Claims = parseClaims(accessToken)

        if (claims[AUTHORITIES_KEY] == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        // 클레임에서 권한 정보 가져오기
        val authorities: Collection<GrantedAuthority?> =
            claims[AUTHORITIES_KEY].toString()
                .split(",")
                .map { role -> SimpleGrantedAuthority(role) }

        // UserDetails 객체를 만들어서 Authentication 리턴
        val principal: UserDetails = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}
