package org.deepforest.dcinside.auth

import org.deepforest.dcinside.entity.auth.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
    fun findByKey(key: String): RefreshToken?
}