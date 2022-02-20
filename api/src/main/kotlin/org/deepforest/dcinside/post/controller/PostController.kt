package org.deepforest.dcinside.post.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.entity.gallery.GalleryType
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
        @RequestParam(required = false) type: GalleryType? = null
    ): ResponseDto<List<PostResponseDto>> = ResponseDto.ok(
        postService.findPost(type)
    )

    @PostMapping
    fun addPosts(
        @RequestBody postRequestDto : PostRequestDto
    ) : ResponseDto<Unit>{
        postService.add(postRequestDto);
        return ResponseDto.ok()
    }


}
