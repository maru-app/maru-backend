package me.daegyeo.maru.file.adapter.out.batch

import io.minio.MinioClient
import io.minio.RemoveObjectArgs
import me.daegyeo.maru.file.application.port.out.DeleteFilePort
import me.daegyeo.maru.file.application.port.out.ReadAllFilePort
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class CleanUpFileObjectsBatch(
    private val deleteFilePort: DeleteFilePort,
    private val readAllFilePort: ReadAllFilePort,
    private val transactionManager: PlatformTransactionManager,
    private val jobRepository: JobRepository,
    private val jobLauncher: JobLauncher,
    private val minioClient: MinioClient,
) {
    @Value("\${minio.bucket-name}")
    private lateinit var bucket: String

    private val logger = LoggerFactory.getLogger(CleanUpFileObjectsBatch::class.java)

    fun cleanUpFileObjectsTasklet(): Tasklet {
        return Tasklet { _, _ ->
            val orphanedFiles = readAllFilePort.readAllFileByStatusIn(listOf(FileStatus.UPLOADED, FileStatus.ORPHANED))
            orphanedFiles.forEach {
                minioClient.removeObject(
                    RemoveObjectArgs
                        .builder()
                        .bucket(bucket).`object`(it.path)
                        .build(),
                )
            }

            deleteFilePort.deleteUploadedOrOrphanedFile()

            logger.info("사용되지 않는 파일 데이터를 삭제했습니다.")

            RepeatStatus.FINISHED
        }
    }

    fun cleanUpFileObjectsStep(): Step {
        return StepBuilder("cleanUpFileObjectsStep", jobRepository)
            .tasklet(cleanUpFileObjectsTasklet(), transactionManager)
            .allowStartIfComplete(true)
            .build()
    }

    fun cleanUpFileObjectsJob(): Job {
        return JobBuilder("cleanUpFileObjectsJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(cleanUpFileObjectsStep())
            .build()
    }

    @Scheduled(cron = "0 0 6 * * 1")
    fun cleanUpFileObjectsJobSchedule() {
        jobLauncher.run(cleanUpFileObjectsJob(), JobParametersBuilder().toJobParameters())
    }
}
