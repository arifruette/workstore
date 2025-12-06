package presentation.route

import domain.usecase.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import org.koin.ktor.ext.inject
import presentation.dto.toResponse
import java.io.File
import java.util.*

fun Application.configureRouting() {
    routing {
        workRoutes()
    }
}

fun Route.workRoutes() {

    val submitWork: SubmitWorkUseCase by inject()
    val getWorkById: GetWorkByIdUseCase by inject()
    val getAllWorks: GetAllWorksUseCase by inject()
    val getWorksByTask: GetWorksByTaskUseCase by inject()
    val getWorksByStudent: GetWorksByStudentUseCase by inject()

    val uploadDirPath = application.environment.config
        .propertyOrNull("storage.uploadsDir")
        ?.getString()
        ?: "uploads"

    val uploadDir = File(uploadDirPath).also { it.mkdirs() }

    route("/works") {
        post {
            val multipart = call.receiveMultipart()

            var taskId: Long? = null
            var firstName: String? = null
            var lastName: String? = null
            var savedFile: File? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "taskId" -> taskId = part.value.toLongOrNull()
                            "firstName" -> firstName = part.value
                            "lastName" -> lastName = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: "work"
                        val safeName = "${UUID.randomUUID()}_$originalFileName"
                        val file = File(uploadDir, safeName)

                        part.provider().copyAndClose(file.writeChannel())

                        savedFile = file
                    }
                    else -> Unit
                }
                part.dispose()
            }

            if (taskId == null || firstName.isNullOrBlank() || lastName.isNullOrBlank() || savedFile == null) {
                savedFile?.delete()
                call.respond(
                    HttpStatusCode.BadRequest,
                    "taskId, firstName, lastName and file are required"
                )
                return@post
            }

            val work = submitWork(
                taskId = taskId!!,
                studentFirstName = firstName,
                studentLastName = lastName,
                filePath = savedFile.absolutePath
            )

            call.respond(HttpStatusCode.Created, work.toResponse())
        }
        get {
            val taskIdParam = call.request.queryParameters["taskId"]
            val firstName = call.request.queryParameters["firstName"]
            val lastName = call.request.queryParameters["lastName"]

            val works = when {
                taskIdParam != null -> {
                    val id = taskIdParam.toLongOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "taskId is invalid")
                        return@get
                    }
                    getWorksByTask(id)
                }
                !firstName.isNullOrBlank() && !lastName.isNullOrBlank() -> {
                    getWorksByStudent(firstName, lastName)
                }
                else -> {
                    getAllWorks()
                }
            }

            call.respond(works.map { it.toResponse() })
        }

        get("{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }

            val work = getWorkById(id)
            if (work == null) {
                call.respond(HttpStatusCode.NotFound, "Work not found")
            } else {
                call.respond(work.toResponse())
            }
        }
    }
}
