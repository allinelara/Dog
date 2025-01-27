package com.allinedelara.dog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allinedelara.domain.useCase.GetDog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(private val getDog: GetDog) : ViewModel()  {

    private val _dog = MutableStateFlow<String?>("")
    val dog: StateFlow<String?> = _dog

    init {
        getDogImage()
    }

    private fun getDogImage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _dog.value = getDog.invoke().firstOrNull()
            }
        }
    }
}