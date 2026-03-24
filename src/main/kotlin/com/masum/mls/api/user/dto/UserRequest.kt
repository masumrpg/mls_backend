package com.masum.mls.api.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// Untuk Registrasi User Baru
data class RegisterUserRequest(
    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Username harus 3-50 karakter")
    val username: String,

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    val email: String,

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    val password: String,

    val phoneNumber: String? = null,

    // Data Account
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val address: String? = null,
    val city: String? = null,
    val bio: String? = null
)

// Untuk Update Profil User
data class UpdateUserRequest(
    @Size(min = 3, max = 50, message = "Username harus 3-50 karakter")
    val username: String? = null,

    val phoneNumber: String? = null,

    // Data Account
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val bio: String? = null
)