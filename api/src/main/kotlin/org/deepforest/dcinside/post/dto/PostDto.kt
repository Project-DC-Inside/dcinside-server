package org.deepforest.dcinside.post.dto

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.entity.post.PostStatistics

class PostResponseDto(
    val id: Long,
    val type: GalleryType,
    val nickname: String,
    val title: String,
    val content: String,
    val postStatistics: PostStatisticsDto
) {
    companion object {
        fun from(post: Post) =
            PostResponseDto(
                post.id!!,
                post.gallery.type,
                post.nickname,
                post.title,
                post.content,
                PostStatisticsDto.from(post.statistics!!)
            )
    }
}

class PostRequestDto(
    val galleryId: Long,
    val nickname: String,
    val title: String,
    val content: String,
    val password: String
) {

    fun toEntity(member: Member, gallery: Gallery): Post =
        Post(
            gallery = gallery,
            member = member,
            nickname = nickname,
            password = password,
            content = content,
            title = title
        )
}

class PostStatisticsDto(
    val viewCount: Long,
    val likeCount: Long,
    val dislikeCount: Long,
    val commentCount: Long
) {
    companion object {
        fun from(postStatistics: PostStatistics) =
            PostStatisticsDto(
                postStatistics.viewCount,
                postStatistics.likeCount,
                postStatistics.dislikeCount,
                postStatistics.commentCount
            )
    }
}
