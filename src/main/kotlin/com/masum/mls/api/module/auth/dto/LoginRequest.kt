package com.masum.mls.api.module.auth.dto

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)