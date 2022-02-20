package org.deepforest.dcinside.post.dto

import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.post.Post

class PostDto(
    val id: Long,
    val type: GalleryType,
    val nickname: String,
    val title: String,
    val content: String,
    val postStatistics: PostStatisticsDto
) {
    companion object {
        fun from(post: Post) =
            PostDto(
                post.id!!,
                post.gallery.type,
                post.nickname,
                post.title,
                post.content,
                PostStatisticsDto.from(post.statistics!!)
            )
    }
}
