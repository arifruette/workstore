package infra.persistence

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object WorkTable : LongIdTable("works") {
    val taskId = long("task_id")
    val studentFirstName = varchar("student_first_name", 255)
    val studentLastName = varchar("student_last_name", 255)
    val submittedAt = timestamp("submitted_at")
    val filePath = varchar("file_path", 1024)
}
