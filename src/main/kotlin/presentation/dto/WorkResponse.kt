package presentation.dto

import domain.model.Work
import kotlinx.serialization.Serializable

@Serializable
data class WorkResponse(
    val id: Long,
    val taskId: Long,
    val studentFirstName: String,
    val studentLastName: String,
    val submittedAt: String,
    val filePath: String
)

fun Work.toResponse() = WorkResponse(
    id = id ?: 0L,
    taskId = taskId,
    studentFirstName = studentFirstName,
    studentLastName = studentLastName,
    submittedAt = submittedAt.toString(),
    filePath = filePath
)
