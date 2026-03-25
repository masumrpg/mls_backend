package com.masum.mls.api.exception

import com.masum.mls.api.constant.StatusCode
import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    // General
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, StatusCode.E_MLS_500, "Internal server error"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, StatusCode.E_MLS_400, "Bad request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, StatusCode.E_MLS_422, "Validation error"),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, StatusCode.E_MLS_401, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, StatusCode.E_MLS_403, "Forbidden"),

    // Data
    NOT_FOUND(HttpStatus.NOT_FOUND, StatusCode.E_MLS_404, "Data not found"),
    CONFLICT(HttpStatus.CONFLICT, StatusCode.E_MLS_409, "Data conflict")
}