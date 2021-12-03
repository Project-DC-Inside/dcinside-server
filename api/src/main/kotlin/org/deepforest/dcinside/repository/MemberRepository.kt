package org.deepforest.dcinside.repository

import org.deepforest.dcinside.entity.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>