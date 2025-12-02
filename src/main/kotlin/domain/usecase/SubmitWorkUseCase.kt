package domain.usecase

import domain.model.Work
import domain.repository.WorkRepository
import java.time.Instant

class SubmitWorkUseCase(
    private val workRepository: WorkRepository
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
            submittedAt = Instant.now(),
            filePath = filePath
        )
        return workRepository.save(work)
    }
}
