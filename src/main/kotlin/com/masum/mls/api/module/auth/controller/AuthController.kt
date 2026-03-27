package com.masum.mls.api.module.auth.controller

import com.masum.mls.api.module.auth.dto.LoginRequest
import com.masum.mls.api.module.auth.service.AuthServiceApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authServiceApi: AuthServiceApi
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        authServiceApi.login(request)

        return ResponseEntity.ok(
            mapOf(
                "message" to "Login success"
            )
        )
    }
}