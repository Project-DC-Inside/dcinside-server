package org.deepforest.dcinside.dto


class Error(
    private val errorCode: ErrorCode,
    private val msg: String? = null
) {
    val code: String
        get() = errorCode.name

    val message: String
        get() = msg ?: errorCode.detail
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
    FORBIDDEN("권한이 없습니다"),

    NOT_FOUND("데이터를 찾을 수 없습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    CONFLICT("잘못된 데이터를 입력했습니다."),
    DUPLICATE("중복 요청을 할 수 없습니다."),

    INTERNAL_SERVER_ERROR("예상하지 못한 에러가 발생했습니다."),
    ;
}
