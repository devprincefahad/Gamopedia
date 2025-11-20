package dev.prince.gamopedia.util

import dev.prince.gamopedia.model.Result

sealed class GamesUiState {
    object Loading : GamesUiState()
    data class Success(val data: List<Result>) : GamesUiState()
    data class Error(val message: String) : GamesUiState()
}
