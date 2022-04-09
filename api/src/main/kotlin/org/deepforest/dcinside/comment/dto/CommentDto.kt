package org.deepforest.dcinside.comment.dto

import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post

class CommentWrittenByMemberDto(
    val content: String
) {
    fun toEntity(post: Post, member: Member) = Comment(content, post, member)
}

class CommentWrittenByNonMemberDto(
    val content: String,
    val nickname: String,
    val password: String
) {
    fun toEntity(post: Post) = Comment(content, post, nickname = nickname, password = password)
}

class CommentAccessDto(
    val commentId: Long,
    val password: String
)
