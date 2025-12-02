package domain.repository

import domain.model.Work

interface WorkRepository {

    suspend fun save(work: Work): Work

    suspend fun getById(id: Long): Work?

    suspend fun getAll(): List<Work>

    suspend fun getByTaskId(taskId: Long): List<Work>

    suspend fun getByStudent(
        firstName: String,
        lastName: String
    ): List<Work>
}
