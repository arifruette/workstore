package di

import domain.repository.WorkRepository
import domain.usecase.*
import infra.repository.WorkRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val workModule = module {

    single { Dispatchers.IO }

    single<WorkRepository> { WorkRepositoryImpl(dbDispatcher = get()) }

    single { SubmitWorkUseCase(get()) }
    single { GetWorkByIdUseCase(get()) }
    single { GetAllWorksUseCase(get()) }
    single { GetWorksByTaskUseCase(get()) }
    single { GetWorksByStudentUseCase(get()) }
}
