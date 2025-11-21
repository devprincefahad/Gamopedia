package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.repo.GamesRepositoryImpl
import dev.prince.gamopedia.util.GameDetailsUiState
import dev.prince.gamopedia.util.GamesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GamesViewModel(
    private val repository: GamesRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState

    private val _detailsState = MutableStateFlow<GameDetailsUiState>(GameDetailsUiState.Loading)
    val detailsState: StateFlow<GameDetailsUiState> = _detailsState

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            _uiState.value = GamesUiState.Loading

            repository.getGames().collect { result ->
                result.fold(
                    onSuccess = { response ->
                        _uiState.value = GamesUiState.Success(response.results)
                    },
                    onFailure = { error ->
                        _uiState.value = GamesUiState.Error(error.message ?: "Unknown error")
                    }
                )
            }
        }
    }

    fun fetchGameDetails(id: Int) {
        viewModelScope.launch {
            _detailsState.value = GameDetailsUiState.Loading

            repository.getGameDetails(id).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        _detailsState.value = GameDetailsUiState.Success(response)
                    },
                    onFailure = { error ->
                        _detailsState.value = GameDetailsUiState.Error(error.message ?: "Unknown error")
                    }
                )
            }
        }
    }

}