package org.deepforest.dcinside.auth

import org.deepforest.dcinside.dto.ApiException
import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.member.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component


@Component
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.findByUsername(username)
            ?.let { createUserDetails(it) }
            ?: throw ApiException(ErrorCode.NOT_FOUND_MEMBER)
    }

    // DB 에 User 값이 존재한다면 userdetails.User 객체로 만들어서 리턴
    private fun createUserDetails(member: Member): User {
        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority(member.role.toString())

        return User(
            member.id.toString(),
            member.password,
            listOf(grantedAuthority)
        )
    }
}