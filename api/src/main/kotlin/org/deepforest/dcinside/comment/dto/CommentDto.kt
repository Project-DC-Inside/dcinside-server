package org.deepforest.dcinside.comment.dto

import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post

class CommentWrittenByMemberDto(
    val content: String
) {
    fun toEntity(post: Post, member: Member) = Comment(content, post, member)
}
