package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.entity.post.PostStatistics
import org.deepforest.dcinside.exception.ApiException
import org.deepforest.dcinside.post.repository.PostStatisticsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostStatisticsService(
    private val postStatisticsRepository: PostStatisticsRepository,
) {

    fun view(postId: Long) {
        findPostStatistics(postId).viewCount++
    }

    fun like(postId: Long) {
        findPostStatistics(postId).likeCount++
    }

    fun cancelLike(postId: Long) {
        findPostStatistics(postId).likeCount--
    }

    fun dislike(postId: Long) {
        findPostStatistics(postId).dislikeCount++
    }

    fun cancelDislike(postId: Long) {
        findPostStatistics(postId).dislikeCount--
    }

    fun findPostStatistics(postId: Long): PostStatistics =
        postStatisticsRepository.findByPostId(postId) ?: throw ApiException(ErrorCode.NOT_FOUND)

    fun save(post: Post) {
        var postStatistics = PostStatistics(post = post)
        postStatisticsRepository.save(postStatistics)
    }
}
