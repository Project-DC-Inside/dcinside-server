package org.deepforest.dcinside.post.dto

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.entity.post.PostStatistics
import org.deepforest.dcinside.helper.TimeFormatHelper
import org.deepforest.dcinside.member.MemberDto

class PostWrittenByMemberDto(
    val galleryId: Long,
    val title: String,
    val content: String,
) {
    fun toEntity(gallery: Gallery, member: Member) = Post(
        title = title,
        content = content,
        nickname = member.nickname,
        member = member,
        gallery = gallery
    )
}

class PostWrittenByNonMemberDto(
    val galleryId: Long,
    val title: String,
    val content: String,
    val nickname: String,
    val password: String,
) {
    fun toEntity(gallery: Gallery) = Post(
        title = title,
        content = content,
        nickname = nickname,
        password = password,
        gallery = gallery
    )
}

class PostUpdateDto(
    val title: String,
    val content: String
)

class PostAccessDto(
    val postId: Long,
    val password: String
)

class PostResponseDto(
    val id: Long,
    val nickname: String,
    val title: String,
    val createdAt: String,
    val updatedAt: String,
    val postStatistics: PostStatisticsDto,
    val writer: MemberDto,
    var content: String?,
) {
    companion object {
        fun from(
            post: Post,
            needContent: Boolean
        ) = PostResponseDto(
            post.id!!,
            post.nickname,
            post.title,
            TimeFormatHelper.from(post.createdAt),
            TimeFormatHelper.from(post.updatedAt),
            PostStatisticsDto.from(post.statistics),
            if (post.writtenByNonMember) MemberDto.from(post.nickname) else MemberDto.from(post.member!!),
            if (needContent) post.content else null
        )
    }
}

class PostStatisticsDto(
    val viewCount: Long,
    val likeCount: Long,
    val dislikeCount: Long,
    val commentCount: Long
) {
    companion object {
        fun from(statistics: PostStatistics) = PostStatisticsDto(
            statistics.viewCount,
            statistics.likeCount,
            statistics.dislikeCount,
            statistics.commentCount
        )
    }
}
