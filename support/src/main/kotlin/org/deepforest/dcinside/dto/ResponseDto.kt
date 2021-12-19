package org.deepforest.dcinside.dto

class ResponseDto<R>(
    val success: Boolean,
    val result: R?,
    val error: Error?
) {

    companion object {
        fun ok(): ResponseDto<Void> {
            return ResponseDto(true, null, null)
        }

        fun <R> ok(result: R): ResponseDto<R> {
            return ResponseDto(true, result, null)
        }

        fun fail(code: String, message: String): ResponseDto<Void> {
            return ResponseDto(false, null, Error(code, message))
        }

        fun <R> fail(code: String): ResponseDto<R> {
            return ResponseDto(false, null, Error(code, null));
        }
    }
}
