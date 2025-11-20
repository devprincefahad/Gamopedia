package dev.prince.gamopedia.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.repo.GamesRepositoryImpl
import dev.prince.gamopedia.util.GamesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GamesViewModel(
    private val repository: GamesRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState

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

}