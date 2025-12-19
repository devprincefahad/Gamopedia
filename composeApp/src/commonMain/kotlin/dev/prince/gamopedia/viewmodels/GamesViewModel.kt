package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.model.Genre
import dev.prince.gamopedia.model.ScreenshotDto
import dev.prince.gamopedia.network.NetworkObserver
import dev.prince.gamopedia.network.NetworkStatus
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.util.GameDetailsUiState
import dev.prince.gamopedia.util.GamesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GamesViewModel(
    private val repository: GamesRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState

    private val _detailsState = MutableStateFlow<GameDetailsUiState>(GameDetailsUiState.Loading)
    val detailsState: StateFlow<GameDetailsUiState> = _detailsState

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    private val _gamesByGenre =
        MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val gamesByGenre: StateFlow<GamesUiState> = _gamesByGenre

    private val _topRatedGames =
        MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val topRatedGames: StateFlow<GamesUiState> = _topRatedGames
    private val _screenshots =
        MutableStateFlow<Result<List<ScreenshotDto>>?>(null)

    val screenshots = _screenshots.asStateFlow()

    fun loadScreenshots(gameId: Int) {
        viewModelScope.launch {
            repository.getGameScreenshots(gameId)
                .collect { _screenshots.value = it }
        }
    }
    init {
        observeGenres()
        refreshGenres()

        observeGames()
        refreshGames()
    }

    val status: StateFlow<Boolean> = networkObserver.observe()
        .map { it == NetworkStatus.Available }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private fun observeGames() {
        viewModelScope.launch {
            repository.observeGames()
                .onStart { _uiState.value = GamesUiState.Loading
                    _topRatedGames.value = GamesUiState.Loading}
                .collect { games ->
                    if (games.isEmpty()) {
                        _uiState.value = GamesUiState.Loading
                        _topRatedGames.value = GamesUiState.Loading
                        return@collect
                    }

                    _uiState.value = GamesUiState.Success(games)

                    val topRatedGames = games
                        .filter { it.rating != null }
                        .sortedByDescending { it.rating }
                        .take(20)

                    _topRatedGames.value =
                        if (topRatedGames.isEmpty())
                            GamesUiState.Error("No recent releases")
                        else
                            GamesUiState.Success(topRatedGames)
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

    private fun observeGenres() {
        viewModelScope.launch {
            repository.observeGenres().collect {
                _genres.value = it
            }
        }
    }

    private fun refreshGenres() {
        viewModelScope.launch {
            repository.refreshGenres()
        }
    }

    fun fetchGamesByGenre(genreId: Int) {
        viewModelScope.launch {
            _gamesByGenre.value = GamesUiState.Loading

            repository.getGamesByGenre(genreId).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        _gamesByGenre.value =
                            GamesUiState.Success(response.results)
                    },
                    onFailure = {
                        _gamesByGenre.value =
                            GamesUiState.Error(it.message ?: "Error")
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isRecentlyReleased(
        released: String?,
        months: Int = 6
    ): Boolean {
        if (released.isNullOrBlank()) return false

        return try {
            val releaseDate = LocalDate.parse(released)

            val today = Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date

            val cutoff = today.minus(DatePeriod(months = months))

            releaseDate > cutoff
        } catch (e: Exception) {
            false
        }
    }


}