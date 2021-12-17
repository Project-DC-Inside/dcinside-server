package org.deepforest.dcinside.auth.dto

import org.deepforest.dcinside.entity.member.*
import org.springframework.security.crypto.password.PasswordEncoder

data class SignupReqDto(
    private val username: String,
    private val password: String,
    private val email: String,
    private val nickname: String,
    private val nicknameType: NicknameType
) {
    fun toMember(passwordEncoder: PasswordEncoder): Member {
        return Member(
            username = username,
            password = passwordEncoder.encode(password),
            role = MemberRole.ROLE_BASIC
        ).apply {
            memberDetail = MemberDetail(
                email = email,
                nickname = Nickname(nickname, nicknameType),
                member = this
            )
        }
    }
}