package org.deepforest.dcinside.auth.dto

import org.deepforest.dcinside.entity.member.*
import org.springframework.security.crypto.password.PasswordEncoder

data class SignupReqDto(
    private val username: String,
    private val password: String,
    private val email: String,
    private val nickname: String,
) {
    fun toMember(passwordEncoder: PasswordEncoder) = Member(
        username = username,
        email = email,
        nickname = nickname,
        password = passwordEncoder.encode(password),
        role = MemberRole.ROLE_FIXED
    )
}
