package org.deepforest.dcinside.dto

class Error(private val errorCode: ErrorCode) {
    val code: String
        get() = errorCode.name

    val message: String
        get() = errorCode.detail
}

enum class ErrorCode(val detail: String) {
    INVALID_REFRESH_TOKEN("리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN("리프레시 토큰이 일치하지 않습니다"),
    NOT_FOUND_REFRESH_TOKEN("리프레시 토큰이 존재하지 않습니다."),

    UNAUTHORIZED_SECURITY_CONTEXT("Security Context 에 인증 정보가 없습니다."),

    NOT_FOUND_AUTHORITIES_KEY("계정 정보가 없는 토큰입니다"),
    NOT_FOUND_MEMBER("존재하지 않는 ID 입니다."),
    CONFLICT_USERNAME("중복된 ID 가 존재합니다."),
    MISMATCH_PASSWORD("비밀번호가 일치하지 않습니다."),
    ;
}
