package me.daegyeo.maru.file.adapter.out.batch

import me.daegyeo.maru.file.application.port.out.DeleteFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager
import java.time.ZonedDateTime

@Configuration
class DeletePendingFilesBatch(
    private val deleteFilePort: DeleteFilePort,
    private val transactionManager: PlatformTransactionManager,
    private val jobRepository: JobRepository,
    private val jobLauncher: JobLauncher,
) {
    private val logger = LoggerFactory.getLogger(DeletePendingFilesBatch::class.java)

    fun deletePendingFilesTasklet(): Tasklet {
        return Tasklet { _, _ ->
            val oneHourAgo = ZonedDateTime.now().minusHours(1)
            deleteFilePort.deleteFileByStatusAndCreatedAtBefore(FileStatus.PENDING, oneHourAgo)
            logger.info("PENDING 상태의 파일 데이터를 삭제했습니다.")
            RepeatStatus.FINISHED
        }
    }

    fun deletePendingFilesStep(): Step {
        return StepBuilder("deletePendingFilesStep", jobRepository)
            .tasklet(deletePendingFilesTasklet(), transactionManager)
            .build()
    }

    fun deletePendingFilesJob(): Job {
        return JobBuilder("deletePendingFilesJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(deletePendingFilesStep())
            .build()
    }

    @Scheduled(cron = "0 0 6 * * ?")
    fun deletePendingFilesJobSchedule() {
        jobLauncher.run(deletePendingFilesJob(), JobParametersBuilder().toJobParameters())
    }
}
