package com.masum.mls.api.module.user.service

import com.masum.mls.api.exception.AppException
import com.masum.mls.api.exception.ErrorCode
import com.masum.mls.api.module.user.dto.ProfileResponse
import com.masum.mls.api.module.user.dto.RegisterUserRequest
import com.masum.mls.api.module.user.dto.UserResponse
import com.masum.mls.module.account.entity.Profile
import com.masum.mls.module.account.service.ProfileService
import com.masum.mls.module.user.entity.User
import com.masum.mls.module.user.enums.Role
import com.masum.mls.module.user.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceApi(
    private val userService: UserService,
    private val profileService: ProfileService,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(
        timeout = 30,
        rollbackFor = [Exception::class]
    )
    fun registerUser(request: RegisterUserRequest): UserResponse {
        // Cek duplikasi
        if (userService.existsByUsername(request.username)) {
            throw AppException(ErrorCode.CONFLICT, "Username sudah digunakan")
        }
        if (userService.existsByEmail(request.email)) {
            throw AppException(ErrorCode.CONFLICT, "Email sudah digunakan")
        }

        val encodedPassword = if (passwordEncoder.encode(request.password) != null) request.password else throw IllegalArgumentException("Encoded password is null!")

        val user = User(
            username = request.username,
            email = request.email,
            password = encodedPassword,
            phoneNumber = request.phoneNumber,
            role = Role.USER,
            isActive = true
        )
        val userCreated = userService.create(user)

        val profile = Profile(
            fullName = request.fullName,
            avatarUrl = request.avatarUrl,
            address = request.address,
            city = request.city,
            postalCode = request.postalCode,
            bio = request.bio,
            user = userCreated
        )
        val createdAccount = profileService.create(profile)

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
            account = user.profile?.let { acc ->
                ProfileResponse(
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