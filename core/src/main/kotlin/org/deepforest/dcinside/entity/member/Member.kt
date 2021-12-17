package org.deepforest.dcinside.entity.member

import javax.persistence.*


@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["username"])]
)
@Entity
class Member(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long? = null,

    @Column(name = "username")
    val username: String,

    @Column(name = "password")
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    val role: MemberRole,

    @OneToOne(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var memberDetail: MemberDetail? = null
)

enum class MemberRole {
    ROLE_NONE, ROLE_BASIC, ROLE_ORANGE, ROLE_BLUE
}