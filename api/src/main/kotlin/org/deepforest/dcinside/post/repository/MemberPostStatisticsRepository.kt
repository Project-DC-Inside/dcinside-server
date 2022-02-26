package org.deepforest.dcinside.post.repository

import org.deepforest.dcinside.entity.post.MemberPostStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPostStatisticsRepository : JpaRepository<MemberPostStatistics, Long> {
    fun findByMemberIdAndPostStatisticsId(memberId: Long, postStatisticsId: Long) : MemberPostStatistics?
}
