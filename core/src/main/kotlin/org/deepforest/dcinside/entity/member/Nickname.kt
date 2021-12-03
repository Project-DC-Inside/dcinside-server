package org.deepforest.dcinside.entity.member

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class Nickname(
    @Column(name = "nickname")
    val value: String,

    @Column(name = "nickname_type")
    @Enumerated(EnumType.STRING)
    val type: NicknameType
)

enum class NicknameType {
    COMMON, DISTINCT
}