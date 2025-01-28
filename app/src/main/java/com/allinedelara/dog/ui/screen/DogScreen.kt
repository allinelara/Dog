package com.allinedelara.dog.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.allinedelara.dog.R
import com.allinedelara.dog.viewModel.DogViewModel
import com.allinedelara.dog.viewModel.UiState
import com.allinedelara.domain.model.Dog

@Composable
fun DogScreen(viewmodel: DogViewModel = hiltViewModel()) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val isFavourite by viewmodel.isFavourite.collectAsStateWithLifecycle()

    DogContent(
        uiState = uiState,
        isFavourite = isFavourite,
        addToFavourites = viewmodel::addToFavourites,
        deleteFromFavourites = viewmodel::deleteFromFavourites
    )

}

@Composable
fun DogContent(
    uiState: UiState,
    isFavourite: Boolean = false,
    addToFavourites: (String) -> Unit = {},
    deleteFromFavourites: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                uiState.dog?.let {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DogImageFromURLWithPlaceHolder(it)
                        Spacer(modifier = Modifier.size(8.dp))
                        if (isFavourite) {
                            Button(onClick = {
                                deleteFromFavourites(it)
                            }) {
                                Text(text = stringResource(id = R.string.delete_from_favourites))
                            }
                        } else {
                            Button(onClick = {
                                addToFavourites(it)
                            }) {
                                Text(text = stringResource(id = R.string.save_to_favourites))
                            }
                        }
                    }
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
    )
}

@PreviewLightDark()
@Composable
internal fun DogContentPreview() {
    DogContent(
        uiState = UiState.Success(
            dog = "https://images.dog.ceo/breeds/terrier-norwich/n02094258_905.jpg"
        ),
    )
}