package org.deepforest.dcinside.post.dto

import org.deepforest.dcinside.entity.post.PostStatistics

class PostStatisticsDto(
    val viewCount: Long = 0L,
    val likeCount: Long = 0L,
    val dislikeCount: Long = 0L,
    val commentCount: Long = 0L
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
