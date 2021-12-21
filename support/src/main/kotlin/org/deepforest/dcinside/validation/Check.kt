package org.deepforest.dcinside.validation

import org.deepforest.dcinside.dto.ApiException
import org.deepforest.dcinside.dto.ErrorCode

inline fun check(value: Boolean, lazyErrorCode: () -> ErrorCode) {
    if (!value) {
        val errorCode = lazyErrorCode()
        throw ApiException(errorCode)
    }
}

inline fun <T: Any> checkNotNull(value: T?, lazyErrorCode: () -> ErrorCode) {
    if (value == null) {
        val errorCode = lazyErrorCode()
        throw ApiException(errorCode)
    }
}