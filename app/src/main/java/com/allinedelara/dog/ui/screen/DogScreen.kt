package com.allinedelara.dog.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.allinedelara.dog.viewModel.DogViewModel
import com.allinedelara.dog.R

@Composable
fun DogScreen(viewmodel: DogViewModel = hiltViewModel()) {

    val dog by viewmodel.dog.collectAsStateWithLifecycle()

    Log.d("*****", dog.toString())
    DogContent(dog)

}

@Composable
fun DogContent(dog: String?) {
    Column {
        if (dog != null) {
            DogImageFromURLWithPlaceHolder(dog)
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