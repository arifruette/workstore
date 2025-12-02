package domain.model

import java.time.Instant

data class Work(
    val id: Long? = null,
    val taskId: Long,
    val studentFirstName: String,
    val studentLastName: String,
    val submittedAt: Instant,
    val filePath: String
)
