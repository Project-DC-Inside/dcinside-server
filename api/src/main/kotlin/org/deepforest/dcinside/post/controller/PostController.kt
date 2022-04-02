package org.deepforest.dcinside.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.post.dto.PostRequestDto
import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.service.PostService
import org.springframework.web.bind.annotation.*

@Tag(name = "/posts", description = "게시글 API")
@RequestMapping("/api/v1/posts")
@RestController
class PostController(
    private val postService: PostService
) {

    @Operation(summary = "특정 갤러리에 속한 Post 리스트 조회")
    @GetMapping
    fun findPosts(
        @RequestParam(required = false) galleryId: Long? = null
    ): ResponseDto<List<PostResponseDto>> = ResponseDto.ok(
        postService.findPostsByGalleryId(galleryId)
    )

    @Operation(summary = "Post 단건 조회")
    @GetMapping("/{postId}")
    fun findPost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<PostResponseDto> = ResponseDto.ok(
        postService.findPost(postId)
    )

    @Operation(summary = "Post 등록 요청")
    @PostMapping
    fun addPost(
        @RequestBody postRequestDto: PostRequestDto
    ): ResponseDto<Unit> = ResponseDto.ok(postService.add(postRequestDto))

    @Operation(summary = "Post 수정 요청")
    @PutMapping("/{postId}")
    fun editPost(
        @PathVariable("postId") postId: Long,
        @RequestBody postRequestDto: PostRequestDto
    ): ResponseDto<Unit> = ResponseDto.ok(postService.update(postId, postRequestDto))

    @Operation(summary = "Post 삭제 요청")
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(postService.delete(postId))

    @Operation(summary = "Post 싫어요 버튼을 누를 때")
    @PutMapping("/{postId}/dislike")
    fun dislikePost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(postService.dislikePost(postId))


    @Operation(summary = "Post 좋아요 버튼을 누를 때")
    @PutMapping("/{postId}/like")
    fun likePost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> = ResponseDto.ok(postService.likePost(postId))
}
