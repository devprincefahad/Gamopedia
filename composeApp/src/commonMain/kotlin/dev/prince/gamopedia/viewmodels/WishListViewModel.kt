package dev.prince.gamopedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.repo.GamesRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WishListViewModel(
    private val repository: GamesRepository
) : ViewModel() {

    val wishlist: StateFlow<List<WishlistEntity>> =
        repository.observeWishlist()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun isWishlistedFlow(id: Int): Flow<Boolean> =
        repository.isWishlisted(id)

    fun addToWishlist(item: WishlistEntity) {
        viewModelScope.launch {
            repository.addToWishlist(item)
        }
    }

    fun removeFromWishlist(item: WishlistEntity) {
        viewModelScope.launch {
            repository.removeFromWishlist(item)
        }
    }

    fun toggleWishlist(item: WishlistEntity) {
        viewModelScope.launch {
            val currentlyAdded = repository.isWishlisted(item.id).first()
            if (currentlyAdded) repository.removeFromWishlist(item)
            else repository.addToWishlist(item)
        }
    }
}