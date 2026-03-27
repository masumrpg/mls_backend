package com.masum.mls.api.module.auth.service

import com.masum.mls.api.module.auth.dto.LoginRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthServiceApi(
    private val authenticationManager: AuthenticationManager
) {

    fun login(request: LoginRequest) {
        val authToken = UsernamePasswordAuthenticationToken(
            request.usernameOrEmail,
            request.password
        )

        authenticationManager.authenticate(authToken)
    }
}