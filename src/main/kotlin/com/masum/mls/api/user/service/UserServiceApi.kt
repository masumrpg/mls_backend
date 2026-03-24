package com.masum.mls.api.user.service

import com.masum.mls.api.user.dto.AccountResponse
import com.masum.mls.api.user.dto.RegisterUserRequest
import com.masum.mls.api.user.dto.UserResponse
import com.masum.mls.module.account.entity.Account
import com.masum.mls.module.account.service.AccountService
import com.masum.mls.module.user.entity.User
import com.masum.mls.module.user.enums.Role
import com.masum.mls.module.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceApi(
    private val userService: UserService,
    private val accountService: AccountService
) {
    @Transactional(
        timeout = 30,
        rollbackFor = [Exception::class]
    )
    fun registerUser(request: RegisterUserRequest): UserResponse {
        // Cek duplikasi
        if (userService.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username sudah digunakan")
        }
        if (userService.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email sudah digunakan")
        }

        val user = User(
            username = request.username,
            email = request.email,
            password = request.password, // Hash password
            phoneNumber = request.phoneNumber,
            role = Role.USER,
            isActive = true
        )
        val userCreated = userService.createUser(user)

        val account = Account(
            fullName = request.fullName,
            avatarUrl = request.avatarUrl,
            address = request.address,
            city = request.city,
            bio = request.bio,
            user = userCreated
        )
        val createdAccount = accountService.createAccount(account)

        return mapToResponse(createdAccount.user)
    }

    // --- HELPER: Mapping Entity ke Response DTO ---
    private fun mapToResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            username = user.username,
            email = user.email,
            phoneNumber = user.phoneNumber,
            role = user.role,
            isActive = user.isActive,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            account = user.account?.let { acc ->
                AccountResponse(
                    id = acc.id!!,
                    fullName = acc.fullName,
                    avatarUrl = acc.avatarUrl,
                    address = acc.address,
                    city = acc.city,
                    postalCode = acc.postalCode,
                    bio = acc.bio,
                    createdAt = acc.createdAt,
                    updatedAt = acc.updatedAt
                )
            }
        )
    }
}