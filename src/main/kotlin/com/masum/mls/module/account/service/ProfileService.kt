package com.masum.mls.module.account.service

import com.masum.mls.module.account.entity.Profile
import com.masum.mls.module.account.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository

) {
    fun create(profile: Profile): Profile {
        return profileRepository.save(profile)
    }
}