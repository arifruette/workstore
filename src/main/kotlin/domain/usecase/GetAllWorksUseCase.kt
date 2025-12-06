package domain.usecase

import domain.model.Work
import domain.repository.WorkRepository

class GetAllWorksUseCase(
    private val workRepository: WorkRepository
) {
    suspend operator fun invoke(): List<Work> =
        workRepository.getAll()
}
