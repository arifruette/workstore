package infra.client

import domain.client.AnalysisServiceClient
import io.ktor.client.*
import io.ktor.client.request.*

class AnalysisServiceHttpClient(
    private val client: HttpClient,
    private val baseUrl: String
) : AnalysisServiceClient {

    override suspend fun analyzeWork(workId: Long) {
        client.post("$baseUrl/internal/works/$workId/analyze")
    }
}
