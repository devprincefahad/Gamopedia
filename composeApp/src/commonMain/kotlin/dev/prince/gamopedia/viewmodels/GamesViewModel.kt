package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.Genre
import dev.prince.gamopedia.model.ParentPlatform
import dev.prince.gamopedia.model.Platform
import dev.prince.gamopedia.model.ScreenshotDto
import dev.prince.gamopedia.network.NetworkObserver
import dev.prince.gamopedia.network.NetworkStatus
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.util.GameDetailsUiState
import dev.prince.gamopedia.util.GamesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _gamesByGenre = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val gamesByGenre: StateFlow<GamesUiState> = _gamesByGenre

    private val _topRatedGames = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val topRatedGames: StateFlow<GamesUiState> = _topRatedGames

    private val _screenshots = MutableStateFlow<Result<List<ScreenshotDto>>?>(null)
    val screenshots = _screenshots.asStateFlow()

    private val _status = MutableStateFlow(false)
    val status: StateFlow<Boolean> = _status

    init {
        observeGenres()
        observeGames()

        // Monitor network & auto refresh
        viewModelScope.launch {
            networkObserver.observe().collect { networkStatus ->
                val isConnected = (networkStatus == NetworkStatus.Available)
                _status.value = isConnected

                if (isConnected) {
                    refreshGenres()
                    refreshGames()
                }
            }
        }
    }

    private fun observeGames() {
        viewModelScope.launch {
            repository.observeGames()
                .collect { games ->
                    if (games.isEmpty()) {
                        _uiState.value = GamesUiState.Loading
                        _topRatedGames.value = GamesUiState.Loading
                        return@collect
                    }

                    _uiState.value = GamesUiState.Success(games)

                    withContext(Dispatchers.Default) {
                        val topRated = games
                            .filter { it.rating != null }
                            .sortedByDescending { it.rating }
                            .take(20)

                        _topRatedGames.value = if (topRated.isEmpty()) {
                            GamesUiState.Error("No recent releases")
                        } else {
                            GamesUiState.Success(topRated)
                        }
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

    private fun observeGenres() {
        viewModelScope.launch {
            repository.observeGenres().collect {
                _genres.value = it
            }
        }
    }

    private fun refreshGenres() {
        viewModelScope.launch {
            try {
                repository.refreshGenres()
            } catch (e: Exception) {
                Logger.e { "${e.message}"}
            }
        }
    }

    fun getGameDetailsFlow(id: Int): Flow<GameDetailsUiState> = flow {
        emit(GameDetailsUiState.Loading)

        repository.observeGameDetails(id).collect { entity ->
            if (entity != null) {
                val platformsList = entity.platforms
                    ?.split(",")
                    ?.map { it.trim() }
                    ?.filter { it.isNotEmpty() }
                    ?.map { ParentPlatform(Platform(-1, it, it.lowercase())) }
                    ?: emptyList()

                emit(
                    GameDetailsUiState.Success(
                        GameDetailsResponse(
                            id = entity.id,
                            name = entity.name,
                            description = entity.description,
                            backgroundImage = entity.backgroundImage,
                            website = entity.website,
                            rating = entity.rating,
                            parentPlatforms = platformsList
                        )
                    )
                )
            }
        }
    }.onStart {
        viewModelScope.launch {
            val result = repository.refreshGameDetails(id)
            result.onFailure { error ->
                Logger.e("$error")
            }
        }
    }

    fun getScreenshotsFlow(gameId: Int): Flow<Result<List<ScreenshotDto>>> = flow {
        repository.getGameScreenshots(gameId).collect {
            emit(it)
        }
    }

    fun fetchGamesByGenre(genreId: Int) {
        viewModelScope.launch {
            _gamesByGenre.value = GamesUiState.Loading

            repository.getGamesByGenre(genreId).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        _gamesByGenre.value = GamesUiState.Success(response.results)
                    },
                    onFailure = {
                        _gamesByGenre.value = GamesUiState.Error(it.message ?: "Error")
                    }
                )
            }
        }
    }

    fun isValidUrl(url: String?): Boolean {
        return !url.isNullOrBlank() &&
                (url.startsWith("http://") || url.startsWith("https://"))
    }
}