package com.masum.mls.module.account.repository

import com.masum.mls.module.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUserId(userId: Long): Optional<Account>
}