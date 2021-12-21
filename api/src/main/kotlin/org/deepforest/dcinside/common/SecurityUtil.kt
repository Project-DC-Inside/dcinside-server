package org.deepforest.dcinside.common

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.exception.ApiException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {
    fun getCurrentMemberId(): Long {
        return SecurityContextHolder.getContext().authentication?.name?.toLong()
            ?: throw ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT)
    }
}
