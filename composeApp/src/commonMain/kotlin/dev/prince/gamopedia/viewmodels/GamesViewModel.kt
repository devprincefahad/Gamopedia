package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.network.NetworkMonitor
import dev.prince.gamopedia.network.NetworkStatus
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.util.GameDetailsUiState
import dev.prince.gamopedia.util.GamesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GamesViewModel(
    private val repository: GamesRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState

    private val _detailsState = MutableStateFlow<GameDetailsUiState>(GameDetailsUiState.Loading)
    val detailsState: StateFlow<GameDetailsUiState> = _detailsState

    val isConnected = networkMonitor.networkStatus
        .map { it == NetworkStatus.Available }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = true
        )

    init {
        observeGames()
        refreshGames()
    }

    private fun observeGames() {
        viewModelScope.launch {
            repository.observeGames()
                .onStart { _uiState.value = GamesUiState.Loading }
                .collect { games ->
                    if (games.isEmpty()) {
                        _uiState.value = GamesUiState.Loading
                    } else {
                        _uiState.value = GamesUiState.Success(games)
                    }
                }
        }
    }

    private fun refreshGames() {
        viewModelScope.launch {
            try {
                repository.refreshGames()
            } catch (e: Exception) {
                if (_uiState.value is GamesUiState.Loading) {
                    _uiState.value = GamesUiState.Error(e.message ?: "Failed to refresh games")
                }
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