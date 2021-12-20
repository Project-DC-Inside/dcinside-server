package org.deepforest.dcinside.exception

import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ApiException::class])
    protected fun handleApiException(e: ApiException): ResponseDto<Void> {
        logger.error("handleApiException : ${e.errorCode} - ${e.errorCode.detail}")
        return ResponseDto.fail(e.errorCode)
    }
}

class ApiException(val errorCode: ErrorCode) : Throwable()
