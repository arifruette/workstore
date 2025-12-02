package domain.usecase

import domain.model.Work
import domain.repository.WorkRepository

class GetWorkByIdUseCase(
    private val workRepository: WorkRepository
) {
    suspend operator fun invoke(id: Long): Work? =
        workRepository.getById(id)
}
