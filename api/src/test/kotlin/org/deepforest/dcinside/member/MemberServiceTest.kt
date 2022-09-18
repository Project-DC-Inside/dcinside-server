package org.deepforest.dcinside.member

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    @DisplayName("memberId 로 Member 정보 가져오기")
    fun test1() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        memberRepository.saveAndFlush(member)

        // when
        val findMember: MemberInfoDto = memberService.findByMemberId(member.id!!)

        // then
        assertThat(findMember.memberType).isEqualTo(MemberType.of(member.role))
        assertThat(findMember.nickname).isEqualTo(member.nickname)
        assertThat(findMember.username).isEqualTo(member.username)
        assertThat(findMember.email).isEqualTo(member.email)
    }

    @Test
    @DisplayName("username 로 Member 정보 가져오기")
    fun test2() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        memberRepository.saveAndFlush(member)

        // when
        val findMember: MemberInfoDto = memberService.findByUsername(member.username)

        // then
        assertThat(findMember.memberType).isEqualTo(MemberType.of(member.role))
        assertThat(findMember.nickname).isEqualTo(member.nickname)
        assertThat(findMember.username).isEqualTo(member.username)
        assertThat(findMember.email).isEqualTo(member.email)
    }
}
