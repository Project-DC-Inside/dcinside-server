package org.deepforest.dcinside.post.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.post.dto.PostDto
import org.deepforest.dcinside.post.service.PostService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "/posts", description = "게시글 API")
@RequestMapping("/posts")
@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping
    fun findPosts(
        @RequestParam(required = false) type: GalleryType? = null
    ): ResponseDto<List<PostDto>> = ResponseDto.ok(
        postService.findPost(type);
    )


}
