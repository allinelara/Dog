package com.allinedelara.dog

import com.allinedelara.dog.viewModel.FavouriteViewModel
import com.allinedelara.dog.viewModel.UiStateFavourite
import com.allinedelara.domain.model.Dog
import com.allinedelara.domain.useCase.DeleteFromFavourite
import com.allinedelara.domain.useCase.GetAllFavoritesDogs
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouriteViewModelTest {

    private val getAllFavoritesDogs = mockk<GetAllFavoritesDogs>(relaxed = true)
    private val deleteFromFavourite = mockk<DeleteFromFavourite>()


    private lateinit var viewModel: FavouriteViewModel


    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        clearAllMocks() // Reset all mocks
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get All Favorites Dogs`() = runTest {

        val list = listOf<Dog>(
            Dog(
                id = 0,
                image = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
            ),
            Dog(
                id = 1,
                image = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
            )
        )

        val listFlow = flowOf(Result.success(list))

        coEvery { getAllFavoritesDogs.invoke() } returns listFlow

        advanceUntilIdle()

        viewModel =
            FavouriteViewModel(
                getAllFavoritesDogs,
                deleteFromFavourite,
            )


        val result = viewModel.uiState.first { it is UiStateFavourite.Success }

        advanceUntilIdle()

        assertTrue(
            "The state should be Success with the correct list",
            result is UiStateFavourite.Success && result.dogs == list
        )

        coVerify(exactly = 1) { getAllFavoritesDogs.invoke() }
    }

    @Test
    fun `delete dog From Favourites`() = runTest {

        val dog = Dog(
            id = 0,
            image = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
        )

        coEvery { deleteFromFavourite.invoke(dog) } just Runs

        viewModel =
            FavouriteViewModel(
                getAllFavoritesDogs,
                deleteFromFavourite,
            )

        viewModel.deleteFromFavourites(dog)

        advanceUntilIdle()

        coVerify(exactly = 1) { deleteFromFavourite.invoke(dog) }

        val result = viewModel.uiState.value
        assertEquals(
            UiStateFavourite.Loading, result
        )

    }


    @Test
    fun `get All Favorites Dogs throws exception`() = runTest {

        val exceptionMessage = "Network failure"

        coEvery { getAllFavoritesDogs.invoke() } returns flowOf(
            Result.failure(
                Exception(
                    exceptionMessage
                )
            )
        )

        advanceUntilIdle()

        viewModel =
            FavouriteViewModel(
                getAllFavoritesDogs,
                deleteFromFavourite,
            )


        val result = viewModel.uiState.first { it is UiStateFavourite.Error }

        advanceUntilIdle()

        assertEquals(UiStateFavourite.Error(exceptionMessage), result)
    }

    @Test
    fun `delete dog From Favourites handles exception from deleteFromFavourite`() = runTest {
        val dog =
            Dog(id = 0, image = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg")
        val exceptionMessage = "Failed to delete from favourites"

        coEvery { deleteFromFavourite.invoke(dog) } throws Exception(exceptionMessage)

        viewModel =
            FavouriteViewModel(
                getAllFavoritesDogs,
                deleteFromFavourite,
            )

        viewModel.deleteFromFavourites(dog)
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteFromFavourite.invoke(dog) }

        val result = viewModel.uiState.value
        assertTrue(
            "The state should be Error with the exception message",
            result is UiStateFavourite.Error && result.message == exceptionMessage
        )

    }

}