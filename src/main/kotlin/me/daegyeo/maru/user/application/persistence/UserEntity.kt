package me.daegyeo.maru.user.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import me.daegyeo.maru.user.application.constant.Vendor
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE user_id = ?")
@SQLRestriction("deleted_at IS NOT NULL")
class UserEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val vendor: Vendor,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = true, name = "deleted_at")
    var deletedAt: ZonedDateTime?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    val userId: UUID? = null,
) : AuditDateTimeEntity()
