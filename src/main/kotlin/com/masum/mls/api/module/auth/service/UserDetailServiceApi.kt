package com.masum.mls.api.module.auth.service

import com.masum.mls.api.module.auth.security.UserDetail
import com.masum.mls.module.user.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailServiceApi(
    private val userService: UserService,
) : UserDetailsService {
    override fun loadUserByUsername(input: String): UserDetails {
        val user = userService.getByUsernameOrEmail(input, input)
            ?: throw UsernameNotFoundException("User not found")
        return UserDetail(user)
    }
}