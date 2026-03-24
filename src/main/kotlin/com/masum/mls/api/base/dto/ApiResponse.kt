package com.masum.mls.api.base.dto

// Response Standar API
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)