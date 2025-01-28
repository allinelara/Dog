package com.allinedelara.dog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allinedelara.domain.useCase.AddToFavourite
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
    private val addToFavourite: AddToFavourite
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getDogImage()
    }

    private fun getDogImage() {
        viewModelScope.launch {
            getDog.invoke().collect { dog ->
                dog.onSuccess {
                    _uiState.value = UiState.Success(it)
                }.onFailure {
                    _uiState.value = UiState.Error(it.message ?: "Unknown error")
                }
            }
        }
    }

    fun addToFavourites(image: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                addToFavourite.invoke(image)
            }
        }
    }
}