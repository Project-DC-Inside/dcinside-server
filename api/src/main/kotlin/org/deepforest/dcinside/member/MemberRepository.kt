package org.deepforest.dcinside.member

import org.deepforest.dcinside.entity.member.Member
import org.springframework.data.jpa.repository.JpaRepository

fun MemberRepository.hasNotUsername(name: String): Boolean = !existsByUsername(name)

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(name: String): Member?
    fun existsByUsername(name: String): Boolean
}