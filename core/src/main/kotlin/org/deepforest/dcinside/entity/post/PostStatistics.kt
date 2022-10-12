package org.deepforest.dcinside.entity.post

import javax.persistence.*

@Table(
    name = "post_statistics", indexes = [
        Index(columnList = "viewCount"),
        Index(columnList = "likeCount"),
        Index(columnList = "commentCount"),
    ]
)
@Entity
class PostStatistics(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_statistics_id")
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @JoinColumn(name = "like_count")
    var likeCount: Long = 0L,

    @JoinColumn(name = "dislike_count")
    var dislikeCount: Long = 0L,

    @JoinColumn(name = "comment_count")
    var commentCount: Long = 0L,

    viewCount: Long = 0L,
) {
    @JoinColumn(name = "view_count")
    var viewCount: Long = viewCount
        protected set

    fun viewCountUp() {
        viewCount++
    }
}
