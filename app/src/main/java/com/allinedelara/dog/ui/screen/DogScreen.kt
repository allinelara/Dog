package com.allinedelara.dog.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.allinedelara.dog.viewModel.DogViewModel
import com.allinedelara.dog.R
import com.allinedelara.dog.viewModel.UiState

@Composable
fun DogScreen(viewmodel: DogViewModel = hiltViewModel()) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    DogContent(uiState = uiState)

}

@Composable
fun DogContent(uiState: UiState) {
    Column {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                uiState.dog?.let {
                    DogImageFromURLWithPlaceHolder(it)
                }
            }

            is UiState.Error -> {
                uiState.message?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                        fontSize = 22.sp,
                    )
                }

            }
        }
    }
}


@Composable
fun DogImageFromURLWithPlaceHolder(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(50.dp),
        colorFilter = ColorFilter.tint(Color.Blue)
    )
}