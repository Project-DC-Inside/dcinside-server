package org.deepforest.dcinside

import org.deepforest.dcinside.member.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ServiceTestBase {

    @Autowired
    protected lateinit var memberRepository: MemberRepository

}
