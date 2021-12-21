package org.deepforest.dcinside.dto

class ResponseDto<R>(
    val success: Boolean,
    val result: R? = null,
    val error: Error? = null
) {

    companion object {
        fun ok(): ResponseDto<Unit> {
            return ResponseDto(true)
        }

        fun <R> ok(result: R): ResponseDto<R> {
            return ResponseDto(true, result)
        }

        fun fail(code: ErrorCode): ResponseDto<Unit> {
            return ResponseDto(false, error = Error(code))
        }
    }
}
