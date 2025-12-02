package domain.usecase

import domain.model.Work
import domain.repository.WorkRepository

class GetWorksByTaskUseCase(
    private val workRepository: WorkRepository
) {
    suspend operator fun invoke(taskId: Long): List<Work> =
        workRepository.getByTaskId(taskId)
}
