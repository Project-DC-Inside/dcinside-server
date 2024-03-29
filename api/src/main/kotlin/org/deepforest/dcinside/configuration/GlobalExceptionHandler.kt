package org.deepforest.dcinside.configuration

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.exception.ApiException
import org.springframework.dao.DataIntegrityViolationException
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

    @ExceptionHandler(IllegalArgumentException::class)
    protected fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.BAD_REQUEST, e.message)
    }

    @ExceptionHandler(IllegalStateException::class)
    protected fun handleIllegalStateException(e: IllegalStateException): ResponseDto<Unit> {
        logger.error("IllegalStateException message", e)
        return ResponseDto.fail(ErrorCode.BAD_REQUEST, e.message)
    }

    @ExceptionHandler(NoSuchElementException::class)
    protected fun handleNoSuchElementException(e: NoSuchElementException): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.NOT_FOUND, e.message)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    protected fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.CONFLICT)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}
