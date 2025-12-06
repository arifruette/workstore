package domain.usecase

import domain.client.AnalysisServiceClient
import domain.model.Work
import domain.repository.WorkRepository
import kotlinx.datetime.Clock

class SubmitWorkUseCase(
    private val workRepository: WorkRepository,
    private val analysisClient: AnalysisServiceClient
) {
    suspend operator fun invoke(
        taskId: Long,
        studentFirstName: String,
        studentLastName: String,
        filePath: String
    ): Work {
        val work = Work(
            taskId = taskId,
            studentFirstName = studentFirstName,
            studentLastName = studentLastName,
            submittedAt = Clock.System.now(),
            filePath = filePath
        )

        val saved = workRepository.save(work)

        analysisClient.analyzeWork(saved.id!!)

        return saved
    }
}
