package infra.repository

import domain.model.Work
import domain.repository.WorkRepository
import infra.persistence.WorkDAO
import infra.persistence.WorkTable
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class WorkRepositoryImpl(
    private val dbDispatcher: CoroutineDispatcher
) : WorkRepository {

    override suspend fun save(work: Work): Work =
        newSuspendedTransaction(dbDispatcher) {
            if (work.id == null) {
                // создание
                val dao = WorkDAO.new {
                    taskId = work.taskId
                    studentFirstName = work.studentFirstName
                    studentLastName = work.studentLastName
                    submittedAt = work.submittedAt
                    filePath = work.filePath
                }
                dao.toDomain()
            } else {
                val dao = WorkDAO.findById(work.id)
                    ?: throw IllegalArgumentException("Work with id ${work.id} not found")

                dao.taskId = work.taskId
                dao.studentFirstName = work.studentFirstName
                dao.studentLastName = work.studentLastName
                dao.submittedAt = work.submittedAt
                dao.filePath = work.filePath

                dao.toDomain()
            }
        }

    override suspend fun getById(id: Long): Work? =
        newSuspendedTransaction(dbDispatcher) {
            WorkDAO.findById(id)?.toDomain()
        }

    override suspend fun getAll(): List<Work> =
        newSuspendedTransaction(dbDispatcher) {
            WorkDAO.all().map { it.toDomain() }
        }

    override suspend fun getByTaskId(taskId: Long): List<Work> =
        newSuspendedTransaction(dbDispatcher) {
            WorkDAO.find { WorkTable.taskId eq taskId }
                .map { it.toDomain() }
        }

    override suspend fun getByStudent(firstName: String, lastName: String): List<Work> =
        newSuspendedTransaction(dbDispatcher) {
            WorkDAO.find {
                (WorkTable.studentFirstName eq firstName) and
                (WorkTable.studentLastName eq lastName)
            }.map { it.toDomain() }
        }

    private fun WorkDAO.toDomain(): Work =
        Work(
            id = this.id.value,
            taskId = this.taskId,
            studentFirstName = this.studentFirstName,
            studentLastName = this.studentLastName,
            submittedAt = this.submittedAt,
            filePath = this.filePath
        )
}
