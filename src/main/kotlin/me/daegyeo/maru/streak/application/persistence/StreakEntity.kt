package me.daegyeo.maru.streak.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import me.daegyeo.maru.user.application.persistence.UserEntity
import java.util.UUID

@Entity
@Table(name = "streaks")
class StreakEntity(
    @Column(nullable = false, name = "streak")
    var streak: Int,

    @Column(nullable = false, name = "best_streak")
    var bestStreak: Int,

    @ManyToOne(targetEntity = UserEntity::class)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    val user: UserEntity? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streak_id")
    val streakId: Long? = null,
) : AuditDateTimeEntity()
