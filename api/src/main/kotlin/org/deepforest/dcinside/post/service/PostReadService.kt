package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostReadService(
    private val postRepository: PostRepository,
) {
    @Transactional
    fun findPostByIdAndIncreaseViewCount(postId: Long): PostResponseDto {
        return postRepository.findByPostId(postId)
            .let {
                it.viewCountUp()
                PostResponseDto.from(it, needContent = true)
            }
    }
}