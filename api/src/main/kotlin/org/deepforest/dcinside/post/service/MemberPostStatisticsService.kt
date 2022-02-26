package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.MemberPostStatistics
import org.deepforest.dcinside.entity.post.PostStatistics
import org.deepforest.dcinside.post.repository.MemberPostStatisticsRepository
import org.springframework.stereotype.Service

@Service
class MemberPostStatisticsService(
    private val memberPostStatisticsRepository: MemberPostStatisticsRepository
) {

    fun find(memberId: Long, postStatisticsId: Long): MemberPostStatistics? =
        memberPostStatisticsRepository.findByMemberIdAndPostStatisticsId(memberId, postStatisticsId)

    fun addDislike(status: Boolean, member: Member, postStatistics: PostStatistics) {
        var memberPostStatistics = MemberPostStatistics(
            member = member,
            postStatistics = postStatistics,
            disliked = status,
            memberId = member.id!!,
            postStatisticsId = postStatistics.id!!
        )
        memberPostStatisticsRepository.save(memberPostStatistics)
    }


    fun addLike(status: Boolean, member: Member, postStatistics: PostStatistics) {
        var memberPostStatistics: MemberPostStatistics = MemberPostStatistics(
            member = member,
            postStatistics = postStatistics,
            liked = status,
            memberId = member.id!!,
            postStatisticsId = postStatistics.id!!
        )
        memberPostStatisticsRepository.save(memberPostStatistics)
    }

}
