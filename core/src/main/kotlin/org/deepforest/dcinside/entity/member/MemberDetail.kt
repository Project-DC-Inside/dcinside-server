package org.deepforest.dcinside.entity.member

import javax.persistence.*


@Table(
    indexes = [Index(columnList = "nickname, nickname_type")]
)
@Entity
class MemberDetail(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_detail_id")
    val id: Long?,

    @Column(name = "email", unique = true)
    val email: String,

    @Column(name = "password")
    val password: String,

    @Embedded
    val nickname: Nickname,

    @OneToOne
    @JoinColumn(name = "member_id")
    val member: Member
)