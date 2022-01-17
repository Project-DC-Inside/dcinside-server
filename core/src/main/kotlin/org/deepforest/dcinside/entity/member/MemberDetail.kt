package org.deepforest.dcinside.entity.member

import javax.persistence.*


@Table(
    indexes = [Index(columnList = "nickname, nickname_type")],
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
@Entity
class MemberDetail(

    @Column(name = "email")
    val email: String,

    @Embedded
    val nickname: Nickname,

    @OneToOne
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_detail_id")
    val id: Long = 0L,
)
