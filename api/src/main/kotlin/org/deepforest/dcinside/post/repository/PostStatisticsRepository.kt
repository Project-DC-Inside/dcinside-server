package org.deepforest.dcinside.post.repository

import org.deepforest.dcinside.entity.post.PostStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface PostStatisticsRepository : JpaRepository<PostStatistics, Long> {
    fun findByPostId(postId: Long): PostStatistics?
}
