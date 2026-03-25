package com.masum.mls.api.dto

import java.time.ZonedDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val code: String,
    val data: T? = null,
    val errors: List<ErrorDetail>? = null,
    val metaData: MetaData
)

data class ErrorDetail(
    val field: String?,
    val message: String,
    val rejectedValue: Any? = null
)

data class MetaData(
    val method: String,
    val path: String,
    val query: String? = null,
    val traceId: String? = null,
    val requestId: String? = null,
    val timestamp: ZonedDateTime = ZonedDateTime.now(),
    val durationMs: Long? = null
)