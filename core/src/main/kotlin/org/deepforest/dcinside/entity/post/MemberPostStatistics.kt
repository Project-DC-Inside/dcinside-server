package org.deepforest.dcinside.entity.post

import org.deepforest.dcinside.entity.member.Member
import javax.persistence.*

@Entity
class MemberPostStatistics(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false, insertable = false)
    var member: Member,

    @Column(name = "member_id")
    var memberId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_statistics_id", updatable = false, insertable = false)
    var postStatistics: PostStatistics,

    @Column(name = "post_statistics_id")
    var postStatisticsId: Long,

    @Column(name = "liked")
    var liked: Boolean? = false,

    @Column(name = "disliked")
    var disliked: Boolean? = false
)
