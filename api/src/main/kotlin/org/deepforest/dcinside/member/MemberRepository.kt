package org.deepforest.dcinside.member

import org.deepforest.dcinside.entity.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(name: String): Member?
}