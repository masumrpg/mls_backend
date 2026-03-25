package com.masum.mls.api.module.user.dto

import com.masum.mls.module.user.enums.Role
import java.time.LocalDateTime

// Response Detail User
data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val role: Role,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val account: ProfileResponse? = null
)

// Response Detail Account
data class ProfileResponse(
    val id: Long,
    val fullName: String?,
    val avatarUrl: String?,
    val address: String?,
    val city: String?,
    val postalCode: String?,
    val bio: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)

// Response List (Untuk Pagination)
data class UserListResponse(
    val content: List<UserResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)