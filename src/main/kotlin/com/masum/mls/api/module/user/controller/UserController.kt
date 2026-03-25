package com.masum.mls.api.module.user.controller

import com.masum.mls.api.dto.ApiResponse
import com.masum.mls.api.constant.StatusCode
import com.masum.mls.api.module.user.dto.RegisterUserRequest
import com.masum.mls.api.module.user.dto.UserResponse
import com.masum.mls.api.module.user.service.UserServiceApi
import com.masum.mls.api.util.buildMeta
import jakarta.servlet.http.HttpServletRequest
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
    fun register(
        request: HttpServletRequest,
        @Valid @RequestBody registerUserRequest: RegisterUserRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userServiceApi.registerUser(registerUserRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                message = "User berhasil didaftarkan",
                code = StatusCode.S_MLS_200,
                data = user,
                metaData = buildMeta(request),
            )
        )
    }
}