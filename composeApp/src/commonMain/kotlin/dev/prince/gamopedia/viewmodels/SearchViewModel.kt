package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.repo.GamesRepositoryImpl
import dev.prince.gamopedia.util.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: GamesRepository
): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState


    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collect { query ->
                    searchGames(query)
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchGames(query: String) {
        _searchState.value = SearchUiState.Loading

        repository.searchGames(query).collect { result ->
            result.fold(
                onSuccess = { response ->
                    _searchState.value = SearchUiState.Success(response.results)
                },
                onFailure = { error ->
                    _searchState.value = SearchUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

}