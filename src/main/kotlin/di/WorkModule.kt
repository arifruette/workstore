package di

import domain.client.AnalysisServiceClient
import domain.repository.WorkRepository
import domain.usecase.*
import infra.client.AnalysisServiceHttpClient
import infra.repository.WorkRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val workModule = module {

    single { Dispatchers.IO }

    single<AnalysisServiceClient> {
        AnalysisServiceHttpClient(
            client = get(),
            baseUrl = "http://analysis-service:8080"
        )
    }

    single<WorkRepository> { WorkRepositoryImpl(dbDispatcher = get()) }

    single { SubmitWorkUseCase(get(), get()) }
    single { GetWorkByIdUseCase(get()) }
    single { GetAllWorksUseCase(get()) }
    single { GetWorksByTaskUseCase(get()) }
    single { GetWorksByStudentUseCase(get()) }
}
