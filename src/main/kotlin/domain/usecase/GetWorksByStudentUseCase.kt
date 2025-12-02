package domain.usecase

import domain.model.Work
import domain.repository.WorkRepository

class GetWorksByStudentUseCase(
    private val workRepository: WorkRepository
) {
    suspend operator fun invoke(firstName: String, lastName: String): List<Work> =
        workRepository.getByStudent(firstName, lastName)
}
