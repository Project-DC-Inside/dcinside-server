package org.deepforest.dcinside.comment.dto

import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.helper.TimeFormatHelper
import org.deepforest.dcinside.member.MemberDto

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

class CommentResponseDto(
    val commentId: Long,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val replies: List<CommentResponseDto>,
    val writer: MemberDto
) {
    constructor(comment: Comment) : this(
        commentId = comment.id!!,
        content = comment.content,
        TimeFormatHelper.from(comment.createdAt),
        TimeFormatHelper.from(comment.updatedAt),
        replies = comment.replies.map { CommentResponseDto(it) },
        writer = comment.member?.let { MemberDto(it) } ?: MemberDto(comment.nickname)
    )
}
