package dev.prince.gamopedia.api

import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.model.GamesByGenreResponse
import dev.prince.gamopedia.model.GenreResponse
import dev.prince.gamopedia.model.ScreenshotsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService(val httpClient: HttpClient) {

    suspend fun getGames(): Result<GameResponse> {
        return try {
            val response = httpClient.get("api/games") {
                url {
                    parameter("key", API_KEY)
                }
            }.body<GameResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGameDetails(id: Int): Result<GameDetailsResponse> {
        return try {
            val response = httpClient.get("api/games/$id") {
                url { parameter("key", API_KEY) }
            }.body<GameDetailsResponse>()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchGames(query: String): GameResponse {
        return httpClient.get("api/games") {
            parameter("key", API_KEY)
            parameter("search", query)
        }.body()
    }

    suspend fun getGenres(): Result<GenreResponse> {
        return try {
            val response = httpClient.get("api/genres") {
                parameter("key", API_KEY)
            }.body<GenreResponse>()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGamesByGenre(genreId: Int): GamesByGenreResponse {
        return httpClient.get("api/games") {
            parameter("key", API_KEY)
            parameter("genres", genreId)
        }.body()
    }

    suspend fun getGameScreenshots(
        id: Int
    ): Result<ScreenshotsResponse> {
        return try {
            val response = httpClient.get("api/games/$id/screenshots") {
                parameter("key", API_KEY)
            }.body<ScreenshotsResponse>()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val API_KEY = "1516e476943b4a8095bc09c465bb77e3"
    }

}