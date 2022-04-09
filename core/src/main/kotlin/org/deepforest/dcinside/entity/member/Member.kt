package org.deepforest.dcinside.entity.member

import org.deepforest.dcinside.entity.BaseEntity
import org.deepforest.dcinside.entity.post.Post
import javax.persistence.*


@Table(
    name = "member",
    uniqueConstraints = [UniqueConstraint(columnNames = ["username"])]
)
@Entity
class Member(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "nickname")
    val nickname: String,

    @Column(name = "password")
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    val role: MemberRole,

    @OneToMany(mappedBy = "member")
    val posts: MutableList<Post> = mutableListOf()
) : BaseEntity()

enum class MemberRole {
    ROLE_FLEXIBLE, ROLE_FIXED, ROLE_ORANGE, ROLE_BLUE, ROLE_ADMIN
}
