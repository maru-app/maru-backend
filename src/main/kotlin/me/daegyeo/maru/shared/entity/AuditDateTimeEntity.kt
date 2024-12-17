package me.daegyeo.maru.shared.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditDateTimeEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: ZonedDateTime? = null

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    val updatedAt: ZonedDateTime? = null
}