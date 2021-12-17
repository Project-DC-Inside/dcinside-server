package org.deepforest.dcinside.common

import org.springframework.security.core.context.SecurityContextHolder
import java.lang.RuntimeException

object SecurityUtil {
    fun getCurrentMemberId(): Long {
        return SecurityContextHolder.getContext().authentication?.name?.toLong()
            ?: throw RuntimeException("Security Context 에 인증 정보가 없습니다.")
    }
}
