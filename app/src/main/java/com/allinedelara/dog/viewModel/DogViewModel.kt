package com.allinedelara.dog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allinedelara.domain.useCase.AddToFavourite
import com.allinedelara.domain.useCase.CheckDogFavourite
import com.allinedelara.domain.useCase.DeleteFromFavouriteByImage
import com.allinedelara.domain.useCase.GetDogRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val dog: String?) : UiState()
    data class Error(val message: String?) : UiState()
}

@HiltViewModel
class DogViewModel @Inject constructor(
    private val getDog: GetDogRemote,
    private val addToFavourite: AddToFavourite,
    private val checkDogFavourite: CheckDogFavourite,
    private val deleteFromFavouriteByImage: DeleteFromFavouriteByImage
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _isFavourite = MutableStateFlow<Boolean>(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite

    init {
        getDogImage()
    }

    private fun getDogImage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getDog.invoke().collect { dog ->
                    dog.onSuccess {
                        _uiState.value = UiState.Success(it)
                        it?.let { _isFavourite.value = checkDogFavourite.invoke(it) }
                    }.onFailure {
                        _uiState.value = UiState.Error(it.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun addToFavourites(image: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                addToFavourite.invoke(image)
                _isFavourite.value = true
            }
        }
    }

    fun deleteFromFavourites(image: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteFromFavouriteByImage.invoke(image)
                _isFavourite.value = false
            }
        }
    }
}