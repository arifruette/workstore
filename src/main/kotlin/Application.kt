package ru.ari

import di.workModule
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.ktor.plugin.Koin
import presentation.factory.DatabaseFactory
import presentation.route.configureRouting

fun Application.module() {
    DatabaseFactory.init(environment.config)

    install(Koin) {
        modules(workModule)
    }

    install(ContentNegotiation) {
        json()
    }

    configureRouting()
}
