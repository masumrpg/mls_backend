package com.masum.mls.module.account.service

import com.masum.mls.module.account.entity.Account
import com.masum.mls.module.account.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository

) {
    fun createAccount(account: Account): Account {
        return accountRepository.save(account)
    }
}