package di

import domain.client.AnalysisServiceClient
import domain.repository.WorkRepository
import domain.usecase.*
import infra.client.AnalysisServiceHttpClient
import infra.repository.WorkRepositoryImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val workModule = module {

    single { Dispatchers.IO }

    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<AnalysisServiceClient> {
        AnalysisServiceHttpClient(
            client = get(),
            baseUrl = "http://analytics-service:8080"
        )
    }

    single<WorkRepository> { WorkRepositoryImpl(dbDispatcher = get()) }

    single { SubmitWorkUseCase(get(), get()) }
    single { GetWorkByIdUseCase(get()) }
    single { GetAllWorksUseCase(get()) }
    single { GetWorksByTaskUseCase(get()) }
    single { GetWorksByStudentUseCase(get()) }
}
