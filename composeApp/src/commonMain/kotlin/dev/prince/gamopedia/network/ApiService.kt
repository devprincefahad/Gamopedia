package dev.prince.gamopedia.network

import dev.prince.gamopedia.model.GameResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService (val httpClient: HttpClient) {

        // https://api.rawg.io/api/games?key=1516e476943b4a8095bc09c465bb77e3

    suspend fun getGames(): Result<GameResponse> {
        return try {
            val response = httpClient.get("api/games") {
                url {
                    parameter("key", "1516e476943b4a8095bc09c465bb77e3")
                }
            }.body<GameResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}