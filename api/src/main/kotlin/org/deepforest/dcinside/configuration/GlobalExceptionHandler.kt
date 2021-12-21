package org.deepforest.dcinside.configuration

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.exception.ApiException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ApiException::class)
    protected fun handleApiException(e: ApiException): ResponseDto<Unit> {
        logger.error("handleApiException : ${e.errorCode} - ${e.errorCode.detail}")
        return ResponseDto.fail(e.errorCode)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun handleBadCredentialsException(e: BadCredentialsException): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.MISMATCH_PASSWORD)
    }
}
