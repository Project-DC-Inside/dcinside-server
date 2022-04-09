package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.gallery.findByGalleryId
import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByGalleryWithPaging
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostReadService(
    private val postRepository: PostRepository,
    private val galleryRepository: GalleryRepository,
) {
    fun findPostsByGalleryId(
        galleryId: Long,
        lastPostId: Long? = Long.MAX_VALUE
    ): List<PostResponseDto> {
        return galleryRepository.findByGalleryId(galleryId)
            .let {
                postRepository.findByGalleryWithPaging(it, lastPostId ?: Long.MAX_VALUE)
            }.map {
                PostResponseDto.from(it, needContent = false)
            }
    }

    fun findPostById(postId: Long): PostResponseDto {
        return postRepository.findByPostId(postId)
            .let {
                PostResponseDto.from(it, needContent = true)
            }
    }
}
