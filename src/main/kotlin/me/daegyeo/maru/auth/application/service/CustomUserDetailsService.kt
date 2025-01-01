package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.auth.application.port.`in`.CustomUserDetailsUseCase
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(private val getUserUseCase: GetUserUseCase) : CustomUserDetailsUseCase {
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String?): UserDetails {
        try {
            val user = getUserUseCase.getUserByEmail(username!!)
            return CustomUserDetails(user.email, user.userId)
        } catch (e: ServiceException) {
            throw UsernameNotFoundException("User not found")
        } catch (e: Exception) {
            throw e
        }
    }
}
