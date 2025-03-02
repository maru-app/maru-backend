package me.daegyeo.maru.diary.adaptor.event.listener

import me.daegyeo.maru.streak.application.domain.event.CreatedStreakEvent
import me.daegyeo.maru.streak.application.port.`in`.AddTodayStreakUseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CreateStreakListener(private val addTodayStreakUseCase: AddTodayStreakUseCase) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onCreatedStreakEvent(event: CreatedStreakEvent) {
        addTodayStreakUseCase.addTodayStreak(event.userId)
    }
}
