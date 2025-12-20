package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.database.GameEntity
import dev.prince.gamopedia.repo.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WishListViewModel(
    private val repository: GamesRepository
) : ViewModel() {

    val wishlist: StateFlow<List<GameEntity>> =
        repository.observeWishlist()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun isWishlistedFlow(id: Int): Flow<Boolean> =
        repository.isWishlisted(id)

    fun toggleWishlist(game: GameEntity) {
        viewModelScope.launch {
            val currentlyAdded = repository.isWishlisted(game.id).first()
            if (currentlyAdded) {
                repository.removeFromWishlist(game.id)
            } else {
                repository.addToWishlist(game)
            }
        }
    }
}