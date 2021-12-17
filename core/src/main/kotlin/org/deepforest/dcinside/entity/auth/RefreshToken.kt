package org.deepforest.dcinside.entity.auth

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "refresh_token")
@Entity
class RefreshToken(

    @Id
    @Column(name = "refresh_token_key")
    private val key: String,

    @Column(name = "refresh_token_value")
    private val value: String
)