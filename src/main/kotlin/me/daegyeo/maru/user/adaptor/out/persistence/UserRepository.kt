package me.daegyeo.maru.user.adaptor.out.persistence

import me.daegyeo.maru.user.application.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): Optional<UserEntity>
}
