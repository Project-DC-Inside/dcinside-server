package org.deepforest.dcinside.post.dto

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post

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
    val id: Long,
    val galleryType: GalleryType,
    val galleryName: String,
    val nickname: String,
    val title: String,
    val content: String,
    val password: String
) {

    fun toEntity(member: Member, gallery: Gallery): Post =
        Post(
            password = password,
            gallery = gallery,
            member = member,
            nickname = nickname,
            content = content,
            title = title
        )
}
