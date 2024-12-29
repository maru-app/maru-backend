package me.daegyeo.maru.shared.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditDateTimeEntity {
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: ZonedDateTime? = null

    @Column(name = "updated_at", nullable = false)
    var updatedAt: ZonedDateTime? = null

    @PrePersist
    fun prePersist() {
        createdAt = ZonedDateTime.now()
        updatedAt = ZonedDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = ZonedDateTime.now()
    }
}
