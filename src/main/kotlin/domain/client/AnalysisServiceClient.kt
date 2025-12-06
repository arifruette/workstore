package domain.client

interface AnalysisServiceClient {
    suspend fun analyzeWork(workId: Long)
}
