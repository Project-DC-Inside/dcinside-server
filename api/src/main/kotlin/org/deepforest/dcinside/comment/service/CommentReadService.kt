package org.deepforest.dcinside.comment.service

import org.deepforest.dcinside.comment.dto.CommentResponseDto
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.repository.findByPostWithPaging
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentReadService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {
    fun findCommentsByPostId(
        postId: Long,
        lastCommentId: Long? = 0L
    ): List<CommentResponseDto> {
        return postRepository.findByPostId(postId)
            .let {
                commentRepository.findByPostWithPaging(it, lastCommentId ?: 0L)
            }.map {
                CommentResponseDto(it)
            }
    }
}
