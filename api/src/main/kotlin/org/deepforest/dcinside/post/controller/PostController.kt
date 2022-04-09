package org.deepforest.dcinside.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.common.SecurityUtil
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.post.dto.*
import org.deepforest.dcinside.post.service.PostForMemberService
import org.deepforest.dcinside.post.service.PostForNonMemberService
import org.deepforest.dcinside.post.service.PostReadService
import org.springframework.web.bind.annotation.*

@Tag(name = "/posts", description = "게시글 API")
@RequestMapping("/api/v1/posts")
@RestController
class PostController(
    val postReadService: PostReadService,
    val postForMemberService: PostForMemberService,
    val postForNonMemberService: PostForNonMemberService
) {

    @Operation(summary = "특정 갤러리에 속한 게시글 리스트 조회", description = "리스트로 내려주기 위해 사이즈를 많이 차지할 것 같은 content 는 미포함")
    @GetMapping
    fun findPosts(
        @RequestParam(required = true) galleryId: Long,
        @RequestParam(required = false) lastPostId: Long? = Long.MAX_VALUE,
    ): ResponseDto<List<PostResponseDto>> = ResponseDto.ok(
        postReadService.findPostsByGalleryId(galleryId, lastPostId)
    )

    @Operation(summary = "Post 단건 조회")
    @GetMapping("/{postId}")
    fun findPost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<PostResponseDto> = ResponseDto.ok(
        postReadService.findPostById(postId)
    )


    @Operation(summary = "회원의 Post 저장 요청")
    @PostMapping
    fun savePostForMember(
        @RequestBody postWrittenByMemberDto: PostWrittenByMemberDto
    ): ResponseDto<Long> = ResponseDto.ok(
        postForMemberService.savePost(postWrittenByMemberDto, SecurityUtil.getCurrentMemberId())
    )

    @Operation(summary = "회원의 Post 수정 요청")
    @PutMapping("/{postId}")
    fun updatePostForMember(
        @PathVariable("postId") postId: Long,
        @RequestBody postUpdateDto: PostUpdateDto
    ): ResponseDto<Long> = ResponseDto.ok(
        postForMemberService.updatePost(postId, postUpdateDto, SecurityUtil.getCurrentMemberId())
    )

    @Operation(summary = "회원의 Post 삭제 요청")
    @DeleteMapping("/{postId}")
    fun deletePostForMember(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(
        postForMemberService.deletePost(postId, SecurityUtil.getCurrentMemberId())
    )


    @Operation(summary = "비회원의 Post 저장 요청")
    @PostMapping("/non-member")
    fun savePostForNonMember(
        @RequestBody postWrittenByNonMemberDto: PostWrittenByNonMemberDto
    ): ResponseDto<Long> = ResponseDto.ok(
        postForNonMemberService.savePost(postWrittenByNonMemberDto)
    )

    @Operation(summary = "비회원의 Post 수정 요청")
    @PutMapping("/non-member/{postId}")
    fun updatePostForNonMember(
        @PathVariable("postId") postId: Long,
        @RequestBody postUpdateDto: PostUpdateDto
    ): ResponseDto<Long> = ResponseDto.ok(
        postForNonMemberService.updatePost(postId, postUpdateDto)
    )

    @Operation(summary = "비회원의 Post 삭제 요청")
    @DeleteMapping("/non-member/{postId}")
    fun deletePostForNonMember(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(
        postForNonMemberService.deletePost(postId)
    )

    @Operation(summary = "비회원의 Post 호출 전 비밀번호 확인 API")
    @PutMapping("/non-member/access")
    fun checkAccess(
        @RequestBody postAccessDto: PostAccessDto
    ): ResponseDto<Boolean> = ResponseDto.ok(
        postForNonMemberService.checkAccess(postAccessDto)
    )
}
