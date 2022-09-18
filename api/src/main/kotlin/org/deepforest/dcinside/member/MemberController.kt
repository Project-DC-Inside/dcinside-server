package org.deepforest.dcinside.member

import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.common.SecurityUtil
import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "/members", description = "회원 API")
@RequestMapping("/api/v1/members")
@RestController
class MemberController(
    val memberService: MemberService
) {

    @GetMapping("/me")
    fun findMyMemberInfo(): ResponseDto<MemberInfoDto> = ResponseDto.ok(
        memberService.findByMemberId(SecurityUtil.getCurrentMemberId())
    )

    @GetMapping("/{username}")
    fun findMemberInfo(@PathVariable username: String): ResponseDto<MemberInfoDto> = ResponseDto.ok(
        memberService.findByUsername(username)
    )
}
