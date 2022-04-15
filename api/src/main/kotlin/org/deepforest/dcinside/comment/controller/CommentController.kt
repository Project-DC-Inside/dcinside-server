package org.deepforest.dcinside.comment.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.comment.dto.CommentAccessDto
import org.deepforest.dcinside.comment.dto.CommentResponseDto
import org.deepforest.dcinside.comment.dto.CommentWrittenByMemberDto
import org.deepforest.dcinside.comment.dto.CommentWrittenByNonMemberDto
import org.deepforest.dcinside.comment.service.CommentForMemberService
import org.deepforest.dcinside.comment.service.CommentForNonMemberService
import org.deepforest.dcinside.comment.service.CommentReadService
import org.deepforest.dcinside.common.SecurityUtil
import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.web.bind.annotation.*

@Tag(name = "/comments", description = "댓글 API")
@RequestMapping("/api/v1")
@RestController
class CommentController(
    val commentReadService: CommentReadService,
    val commentForMemberService: CommentForMemberService,
    val commentForNonMemberService: CommentForNonMemberService
) {
    @Operation(summary = "특정 게시글의 댓글 조회")
    @GetMapping("/posts/{postId}/comments")
    fun findCommentsByPost(
        @PathVariable postId: Long,
        @RequestParam(required = false) lastCommentId: Long? = 0L
    ): ResponseDto<List<CommentResponseDto>> = ResponseDto.ok(
        commentReadService.findCommentsByPostId(postId, lastCommentId)
    )


    @Operation(summary = "회원이 특정 게시글에 댓글 저장 요청")
    @PostMapping("/posts/{postId}/comments")
    fun saveCommentForMember(
        @PathVariable postId: Long,
        @RequestBody dto: CommentWrittenByMemberDto
    ): ResponseDto<Long> = ResponseDto.ok(
        commentForMemberService.saveComment(dto, postId, SecurityUtil.getCurrentMemberId())
    )

    @Operation(summary = "회원이 특정 게시글에 단 댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    fun deleteCommentForMember(
        @PathVariable commentId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(
        commentForMemberService.deleteComment(commentId, SecurityUtil.getCurrentMemberId())
    )


    @Operation(summary = "비회원이 특정 게시글에 댓글 저장 요청")
    @PostMapping("/posts/{postId}/comments/non-member")
    fun saveCommentForNonMember(
        @PathVariable postId: Long,
        @RequestBody dto: CommentWrittenByNonMemberDto
    ): ResponseDto<Long> = ResponseDto.ok(
        commentForNonMemberService.saveComment(dto, postId)
    )

    @Operation(summary = "비회원이 특정 게시글에 단 댓글 삭제")
    @DeleteMapping("/comments/non-member/{commentId}")
    fun deleteCommentForNonMember(
        @PathVariable commentId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(
        commentForNonMemberService.deleteComment(commentId)
    )

    @Operation(summary = "비회원의 Comment 삭제 호출 전 비밀번호 확인 API")
    @PutMapping("/comments/non-member/access")
    fun checkAccess(
        @RequestBody commentAccessDto: CommentAccessDto
    ): ResponseDto<Boolean> = ResponseDto.ok(
        commentForNonMemberService.checkAccess(commentAccessDto)
    )
}
