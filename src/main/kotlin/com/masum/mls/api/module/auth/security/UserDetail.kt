package com.masum.mls.api.module.auth.security

import com.masum.mls.module.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetail(
    private val user: User
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> { return listOf(SimpleGrantedAuthority("ROLE_${user.role.name}")) }
    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.username // default
    fun getEmail(): String = user.email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = user.isActive
}