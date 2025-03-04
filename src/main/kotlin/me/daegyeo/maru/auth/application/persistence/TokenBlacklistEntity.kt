package me.daegyeo.maru.auth.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity

@Entity
@Table(
    name = "token_blacklist",
    indexes = [Index(name = "idx_token", columnList = "token")],
)
class TokenBlacklistEntity(
    @Column(nullable = false, columnDefinition = "VARCHAR(512)")
    val token: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_blacklist_id")
    val tokenBlacklistId: Long? = null,
) : AuditDateTimeEntity()
