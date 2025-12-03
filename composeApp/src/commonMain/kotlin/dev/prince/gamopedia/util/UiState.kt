package dev.prince.gamopedia.util

import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.Result

sealed class GamesUiState {
    object Loading : GamesUiState()
    data class Success(val data: List<Result>) : GamesUiState()
    data class Error(val message: String) : GamesUiState()
}

sealed class GameDetailsUiState {
    object Loading : GameDetailsUiState()
    data class Success(val data: GameDetailsResponse) : GameDetailsUiState()
    data class Error(val message: String) : GameDetailsUiState()
}

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val data: List<Result>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
