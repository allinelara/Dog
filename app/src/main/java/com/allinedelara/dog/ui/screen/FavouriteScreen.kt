package com.allinedelara.dog.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.allinedelara.dog.R
import com.allinedelara.dog.ui.component.DogImageFromURLWithPlaceHolder
import com.allinedelara.dog.viewModel.FavouriteViewModel
import com.allinedelara.dog.viewModel.UiStateFavourite
import com.allinedelara.domain.model.Dog


@Composable
fun FavoriteScreen(viewmodel: FavouriteViewModel = hiltViewModel()) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    ContentFavourite(
        uiState = uiState, deleteFromFavourites = viewmodel::deleteFromFavourites
    )
}

@Composable
fun ContentFavourite(
    uiState: UiStateFavourite, deleteFromFavourites: (Dog) -> Unit = {},
) {
    when (uiState) {
        is UiStateFavourite.Error -> uiState.message?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                fontSize = 22.sp,
            )
        }

        UiStateFavourite.Loading -> CircularProgressIndicator()
        is UiStateFavourite.Success -> {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.dogs) { dog ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DogImageFromURLWithPlaceHolder(
                            width = 300,
                            height = 200,
                            imageUrl = dog.image
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(onClick = {
                            deleteFromFavourites(dog)
                        }) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            }
        }
    }
}

