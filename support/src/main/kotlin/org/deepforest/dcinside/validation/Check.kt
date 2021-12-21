package org.deepforest.dcinside.validation

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.exception.ApiException

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