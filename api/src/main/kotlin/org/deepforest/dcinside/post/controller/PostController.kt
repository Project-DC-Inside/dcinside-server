package org.deepforest.dcinside.post.controller

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

    @GetMapping
    fun findPosts(
        @RequestParam(required = false) galleryId: Long? = null
    ): ResponseDto<List<PostResponseDto>> = ResponseDto.ok(
        postService.findPostsByGalleryId(galleryId)
    )

    @GetMapping("/{postId}")
    fun findPost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<PostResponseDto> = ResponseDto.ok(
        postService.findPost(postId)
    )

    @PostMapping
    fun addPost(
        @RequestBody postRequestDto: PostRequestDto
    ): ResponseDto<Unit> {
        postService.add(postRequestDto);
        return ResponseDto.ok()
    }

    @PutMapping("/{postId}")
    fun editPost(
        @PathVariable("postId") postId: Long,
        @RequestBody postRequestDto: PostRequestDto
    ): ResponseDto<Unit> {
        postService.update(postId, postRequestDto);
        return ResponseDto.ok();
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> {
        postService.delete(postId);
        return ResponseDto.ok();
    }

    @PutMapping("/{postId}/dislike")
    fun dislikePost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> {
        postService.dislikePost(postId);
        return ResponseDto.ok();
    }

    @PutMapping("/{postId}/like")
    fun likePost(
        @PathVariable("postId") postId: Long
    ): ResponseDto<Unit> {
        postService.likePost(postId);
        return ResponseDto.ok();
    }

}
