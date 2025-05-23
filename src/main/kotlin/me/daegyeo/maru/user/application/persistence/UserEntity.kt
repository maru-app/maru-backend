package me.daegyeo.maru.user.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = now(), email = '<deleted-account>@maruu.space' WHERE user_id = ?")
@SQLRestriction("deleted_at IS NULL")
class UserEntity(
    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val vendor: Vendor,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false)
    @ColumnDefault("false")
    var isPublicRanking: Boolean = false,

    @Column(nullable = true, name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    val userId: UUID? = null,
) : AuditDateTimeEntity()
