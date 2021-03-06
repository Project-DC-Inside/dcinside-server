package org.deepforest.dcinside.entity.auth

import org.deepforest.dcinside.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "refresh_token")
@Entity
class RefreshToken(

    @Id
    @Column(name = "refresh_token_key")
    val key: String,

    @Column(name = "refresh_token_value")
    var value: String
) : BaseEntity() {
    fun updateValue(token: String) {
        this.value = token
    }
}
