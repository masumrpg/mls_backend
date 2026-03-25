package com.masum.mls.api.exception

class AppException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.message
) : RuntimeException(message)