package org.deepforest.dcinside.member

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun findByMemberId(memberId: Long): MemberInfoDto {
        return MemberInfoDto.from(memberRepository.findByMemberId(memberId))
    }

    fun findByUsername(username: String): MemberInfoDto {
        return MemberInfoDto.from(memberRepository.findByUsername(username)!!)
    }
}
