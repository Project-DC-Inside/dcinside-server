package org.deepforest.dcinside.configuration

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ApiException::class])
    protected fun handleApiException(e: ApiException): ResponseDto<Unit> {
        logger.error("handleApiException : ${e.errorCode} - ${e.errorCode.detail}")
        return ResponseDto.fail(e.errorCode)
    }

    @ExceptionHandler(value = [BadCredentialsException::class])
    protected fun handleBadCredentialsException(e: BadCredentialsException): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.MISMATCH_PASSWORD)
    }

    @ExceptionHandler(value = [UsernameNotFoundException::class])
    protected fun handleUsernameNotFoundException(e: UsernameNotFoundException): ResponseDto<Unit> {
        logger.error("message", e)
        return ResponseDto.fail(ErrorCode.NOT_FOUND_MEMBER)
    }
}

class ApiException(val errorCode: ErrorCode) : Throwable()
