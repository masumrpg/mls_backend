package com.masum.mls.api.user.controller

import com.masum.mls.api.base.dto.ApiResponse
import com.masum.mls.api.user.dto.RegisterUserRequest
import com.masum.mls.api.user.dto.UserResponse
import com.masum.mls.api.user.service.UserServiceApi
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userServiceApi: UserServiceApi
) {
    // POST /api/v1/users/register
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterUserRequest): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userServiceApi.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(success = true, message = "User berhasil didaftarkan", data = user)
        )
    }
}