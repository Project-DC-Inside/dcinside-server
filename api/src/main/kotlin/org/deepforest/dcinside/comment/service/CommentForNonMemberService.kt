package org.deepforest.dcinside.comment.service

import org.deepforest.dcinside.comment.dto.CommentAccessDto
import org.deepforest.dcinside.comment.dto.CommentWrittenByNonMemberDto
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.repository.findByCommentId
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentForNonMemberService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {
    fun saveComment(dto: CommentWrittenByNonMemberDto, postId: Long): Long {
        val post = postRepository.findByPostId(postId)
        val comment = dto.toEntity(post)
        return commentRepository.save(comment).id!!
    }

    fun deleteComment(commentId: Long) {
        commentRepository.findByCommentId(commentId).also {
            commentRepository.delete(it)
        }
    }

    @Transactional(readOnly = true)
    fun checkAccess(dto: CommentAccessDto): Boolean {
        return commentRepository.findByCommentId(dto.commentId)
            .isSamePassword(dto.password)
    }
}
