package org.deepforest.dcinside.entity.comment

import org.deepforest.dcinside.entity.BaseEntity
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post
import javax.persistence.*

@Table(name = "comment", indexes = [Index(columnList = "created_at")])
@Entity
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Column(name = "nickname", updatable = false)
    val nickname: String,

    @Column(name = "password", updatable = false)
    val password: String,

    @Column(name = "content", columnDefinition = "text")
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_comment_id")
    val baseComment: Comment?
) : BaseEntity()
