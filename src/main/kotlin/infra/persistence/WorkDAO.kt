package infra.persistence

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WorkDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<WorkDAO>(WorkTable)

    var taskId by WorkTable.taskId
    var studentFirstName by WorkTable.studentFirstName
    var studentLastName by WorkTable.studentLastName
    var submittedAt by WorkTable.submittedAt
    var filePath by WorkTable.filePath
}