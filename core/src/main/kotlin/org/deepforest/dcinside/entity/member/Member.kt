package org.deepforest.dcinside.entity.member

import javax.persistence.*

@Entity
class Member(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long?,

    @Column(name = "username", unique = true)
    val username: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    val role: MemberRole,

    @OneToOne(mappedBy = "member")
    val memberDetail: MemberDetail?
) {
    constructor(username: String, role: MemberRole): this(null, username, role, null)
}

enum class MemberRole {
    NONE, BASIC, ORANGE, BLUE
}