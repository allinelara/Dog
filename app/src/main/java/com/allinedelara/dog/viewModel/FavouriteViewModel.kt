package com.allinedelara.dog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allinedelara.domain.model.Dog
import com.allinedelara.domain.useCase.DeleteFromFavourite
import com.allinedelara.domain.useCase.GetAllFavoritesDogs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed class UiStateFavourite {
    object Loading : UiStateFavourite()
    data class Success(val dogs: List<Dog>) : UiStateFavourite()
    data class Error(val message: String?) : UiStateFavourite()
}

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getAllFavoritesDogs: GetAllFavoritesDogs,
    private val deleteFromFavourite: DeleteFromFavourite
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<UiStateFavourite>(UiStateFavourite.Loading)
    val uiState: StateFlow<UiStateFavourite> = _uiState

    init {
        getDogs()
    }

    private fun getDogs() {
        viewModelScope.launch {
            getAllFavoritesDogs.invoke().collect { dogs ->
                dogs.onSuccess {
                    _uiState.value = UiStateFavourite.Success(it)
                }.onFailure {
                    _uiState.value = UiStateFavourite.Error(it.message ?: "Unknown error")
                }
            }
        }
    }

    fun deleteFromFavourites(dog: Dog) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteFromFavourite.invoke(dog)
                _uiState.value = UiStateFavourite.Loading
                getDogs()
            }
        }
    }
}