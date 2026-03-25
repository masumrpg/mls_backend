package com.masum.mls.module.account.repository

import com.masum.mls.module.account.entity.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProfileRepository : JpaRepository<Profile, Long> {
    fun findByUserId(userId: Long): Optional<Profile>
}