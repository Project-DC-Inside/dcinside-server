package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.post.dto.PostDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository : PostRepository
) {
    fun findPost(galleryType: GalleryType?): List<PostDto> {
        postRepository.findByGalleryType(galleryType);
    }
}
