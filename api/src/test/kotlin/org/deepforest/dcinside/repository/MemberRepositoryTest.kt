package org.deepforest.dcinside.repository

import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.member.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class MemberRepositoryTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun test() {
        memberRepository.save(
            Member(username = "woody", password = "12345", role = MemberRole.ROLE_NONE)
        )
    }
}