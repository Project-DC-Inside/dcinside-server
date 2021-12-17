package org.deepforest.dcinside.auth

import org.deepforest.dcinside.entity.auth.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
    fun findByKey(key: String): Optional<RefreshToken>
}