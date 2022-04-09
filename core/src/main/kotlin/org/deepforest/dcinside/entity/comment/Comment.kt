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
    val id: Long? = null,

    @Column(name = "nickname", updatable = false)
    val nickname: String,

    @Column(name = "content", columnDefinition = "text")
    val content: String,

    @Column(name = "password", updatable = false)
    val password: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_comment_id")
    val baseComment: Comment? = null
) : BaseEntity() {

    fun writtenBy(other: Member): Boolean = (other == this.member)

    // 회원이 작성한 댓글
    constructor(content: String, post: Post, member: Member) : this(
        id = null,
        nickname = member.nickname,
        content = content,
        password = null,
        post = post,
        member = member,
        baseComment = null
    )

    // 비회원이 작성한 댓글
    constructor(content: String, post: Post, nickname: String, password: String) : this(
        id = null,
        nickname = nickname,
        content = content,
        password = password,
        post = post,
        member = null,
        baseComment = null
    )
}
