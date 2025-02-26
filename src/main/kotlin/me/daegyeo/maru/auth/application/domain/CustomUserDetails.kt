package me.daegyeo.maru.auth.application.domain

import me.daegyeo.maru.shared.constant.Vendor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.ZonedDateTime
import java.util.UUID

class CustomUserDetails(
    private val email: String,
    val userId: UUID,
    val nickname: String,
    val vendor: Vendor,
    val createdAt: ZonedDateTime,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf("ROLE_USER").map { SimpleGrantedAuthority(it) }
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
