package org.deepforest.dcinside.dto

class ResponseDto<R>(
    val success: Boolean,
    val result: R?
) {

    companion object {
        fun ok(): ResponseDto<Void> {
            return ResponseDto(true, null)
        }

        fun <R> ok(result: R): ResponseDto<R> {
            return ResponseDto(true, result)
        }

        fun fail(): ResponseDto<Void> {
            return ResponseDto(false, null)
        }

        fun <R> fail(result: R): ResponseDto<R> {
            return ResponseDto(false, result);
        }
    }
}
