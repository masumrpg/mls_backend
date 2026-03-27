package com.masum.mls.module.user.service

import com.masum.mls.module.user.entity.User
import com.masum.mls.module.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun create(user: User): User {
        return userRepository.save(user)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun getByUsernameOrEmail(username: String, email: String): User? {
        return userRepository.findByUsernameOrEmail(username, email)
    }
}