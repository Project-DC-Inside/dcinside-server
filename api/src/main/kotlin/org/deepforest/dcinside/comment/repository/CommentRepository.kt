package org.deepforest.dcinside.comment.repository

import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.post.Post
import org.springframework.data.jpa.repository.JpaRepository

fun CommentRepository.findByCommentId(id: Long): Comment =
    findById(id).orElseThrow { NoSuchElementException("id: $id 댓글 정보가 없습니다") }

fun CommentRepository.findByPostWithPaging(post: Post, commentId: Long = 0L): List<Comment> =
    findTop20ByPostAndIdGreaterThanAndBaseCommentIsNull(post, commentId)

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findTop20ByPostAndIdGreaterThanAndBaseCommentIsNull(post: Post, commentId: Long): List<Comment>
}
