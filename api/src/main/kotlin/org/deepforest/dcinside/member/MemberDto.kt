package org.deepforest.dcinside.member

import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.member.MemberRole.*

class MemberDto(
    val memberType: MemberType,
    val nickname: String,
    val username: String? = null
) {
    // 회원
    constructor(member: Member) : this(
        memberType = MemberType.of(member.role),
        nickname = member.nickname,
        username = member.username
    )

    // 비회원
    constructor(nickname: String) : this(
        memberType = MemberType.NONE,
        nickname = nickname
    )

    companion object {
        // 회원
        fun from(member: Member) = MemberDto(
            MemberType.of(member.role),
            member.nickname,
            member.username
        )

        // 비회원
        fun from(nickname: String) = MemberDto(
            MemberType.NONE,
            nickname
        )
    }
}

enum class MemberType {
    NONE, FLEXIBLE, FIXED, ORANGE, BLUE, ADMIN;

    companion object {
        fun of(role: MemberRole) = when (role) {
            ROLE_FLEXIBLE -> FLEXIBLE
            ROLE_FIXED -> FIXED
            ROLE_ORANGE -> ORANGE
            ROLE_BLUE -> BLUE
            ROLE_ADMIN -> ADMIN
        }
    }
}
