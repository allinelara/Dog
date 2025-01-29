package com.allinedelara.dog

import com.allinedelara.dog.viewModel.DogViewModel
import com.allinedelara.dog.viewModel.UiState
import com.allinedelara.domain.useCase.AddToFavourite
import com.allinedelara.domain.useCase.CheckDogFavourite
import com.allinedelara.domain.useCase.DeleteFromFavouriteByImage
import com.allinedelara.domain.useCase.GetDogRemote
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class DogViewModelTest {

    private val getDogRemote = mockk<GetDogRemote>(relaxed = true)
    private val checkDogFavourite = mockk<CheckDogFavourite>(relaxed = true)
    private val addToFavourite = mockk<AddToFavourite>()
    private val deleteFromFavouriteByImage = mockk<DeleteFromFavouriteByImage>()

    private lateinit var viewModel: DogViewModel


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
    fun `get Dog Remote`() = runTest {

        val expectedUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
        val urlFlow = flowOf(Result.success(expectedUrl))

        coEvery { getDogRemote.invoke() } returns urlFlow

        advanceUntilIdle()

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )

        // Wait for the ViewModel to process the flow
        while (viewModel.uiState.value == UiState.Loading) {
            advanceUntilIdle()
        }

        val result = viewModel.uiState.first()

        advanceUntilIdle()

        assertTrue(
            "The emitted URL does not match the expected value",
            result is UiState.Success && result.dog == expectedUrl,
        )
    }

    @Test
    fun `get Dog Remote with empty string`() = runTest {

        val expectedUrl = ""
        val urlFlow = flowOf(Result.success(expectedUrl))

        coEvery { getDogRemote.invoke() } returns urlFlow

        advanceUntilIdle()

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )

        // Wait for the ViewModel to process the flow
        while (viewModel.uiState.value == UiState.Loading) {
            advanceUntilIdle()
        }

        val result = viewModel.uiState.first()

        advanceUntilIdle()

        assertTrue(
            "The emitted URL does not match the expected value",
            result is UiState.Success && result.dog == expectedUrl,
        )
    }

    @Test
    fun `get Dog Remote with error`() = runTest {

        val exceptionMessage = "Network failure"

        coEvery { getDogRemote.invoke() } returns flowOf(Result.failure(Exception(exceptionMessage)))

        advanceUntilIdle()

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )

        val result = viewModel.uiState.first { it is UiState.Error }

        advanceUntilIdle()

        assertEquals(UiState.Error(exceptionMessage), result)
    }


    @Test
    fun `get Dog Remote with null string`() = runTest {

        val expectedUrl = null
        val urlFlow = flowOf(Result.success(expectedUrl))

        coEvery { getDogRemote.invoke() } returns urlFlow

        advanceUntilIdle()

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )

        // Wait for the ViewModel to process the flow
        while (viewModel.uiState.value == UiState.Loading) {
            advanceUntilIdle()
        }

        val result = viewModel.uiState.first()

        advanceUntilIdle()

        assertTrue(
            "The emitted URL does not match the expected value",
            result is UiState.Success && result.dog == expectedUrl,
        )
    }

    @Test
    fun `check Dog is Favourite`() = runTest {

        val expectedUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
        val urlFlow = flowOf(Result.success(expectedUrl))

        coEvery { getDogRemote.invoke() } returns urlFlow

        val isFavourite = true

        coEvery { checkDogFavourite.invoke(expectedUrl) } returns isFavourite


        advanceUntilIdle() // Wait for the ViewModel's coroutines to complete

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )

        // Wait for the ViewModel to process the flow
        while (viewModel.isFavourite.value == false) {
            advanceUntilIdle()
        }

        val result = viewModel.isFavourite.first()


        advanceUntilIdle()


        assertEquals(isFavourite, result)
    }

    @Test
    fun `check Dog is not Favourite`() = runTest {

        val expectedUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
        val urlFlow = flowOf(Result.success(expectedUrl))

        coEvery { getDogRemote.invoke() } returns urlFlow


        val isFavourite = false //is not a favourite

        coEvery { checkDogFavourite.invoke(expectedUrl) } returns isFavourite

        advanceUntilIdle() // Wait for the ViewModel's coroutines to complete

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )


        advanceUntilIdle()

        val result = viewModel.isFavourite.first()

        advanceUntilIdle()


        assertEquals(isFavourite, result)
    }

    @Test
    fun `delete dog From Favourites`() = runTest {

        val imageUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"

        // Mock AddToDb behavior
        coEvery { deleteFromFavouriteByImage.invoke(imageUrl) } just Runs

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )
        // Call the ViewModel method
        viewModel.deleteFromFavourites(imageUrl)

        advanceUntilIdle()

        // Verify that AddToDb was called with the correct parameter
        coVerify(exactly = 1) { deleteFromFavouriteByImage.invoke(imageUrl) }

        val isFavourite = viewModel.isFavourite.value
        assertFalse("isFavourite should be false after adding to favourites", isFavourite)

    }

    @Test
    fun `add dog To Favourite`() = runTest {
        val imageUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"

        // Mock AddToDb behavior
        coEvery { addToFavourite.invoke(imageUrl) } just Runs

        viewModel =
            DogViewModel(
                getDogRemote,
                addToFavourite,
                checkDogFavourite,
                deleteFromFavouriteByImage
            )
        // Call the ViewModel method
        viewModel.addToFavourites(imageUrl)

        advanceUntilIdle()

        // Verify that AddToDb was called with the correct parameter
        coVerify(exactly = 1) { addToFavourite.invoke(imageUrl) }

        val isFavourite = viewModel.isFavourite.value
        assertTrue("isFavourite should be true after adding to favourites", isFavourite)

    }

    @Test
    fun `delete dog From Favourites handles exception from deleteFromFavouriteByImage`() = runTest {
        val image = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
        val exceptionMessage = "Failed to delete from favourites"

        coEvery { deleteFromFavouriteByImage.invoke(image) } throws Exception(exceptionMessage)

        viewModel = DogViewModel(
            getDogRemote,
            addToFavourite,
            checkDogFavourite,
            deleteFromFavouriteByImage
        )

        viewModel.deleteFromFavourites(image)
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteFromFavouriteByImage.invoke(image) }

        val isFavourite = viewModel.isFavourite.value

        assertTrue("isFavourite should remain true when deleting to favourites fails", isFavourite)
    }

    @Test
    fun `addToFavourites handles exception from AddToFavourite`() = runTest {
        val image = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1370.jpg"
        val exceptionMessage = "Failed to add to favourites"

        coEvery { addToFavourite.invoke(image) } throws Exception(exceptionMessage)

        viewModel = DogViewModel(
            getDogRemote,
            addToFavourite,
            checkDogFavourite,
            deleteFromFavouriteByImage
        )

        viewModel.addToFavourites(image)
        advanceUntilIdle()

        coVerify(exactly = 1) { addToFavourite.invoke(image) }

        val isFavourite = viewModel.isFavourite.value
        assertFalse("isFavourite should remain false when adding to favourites fails", isFavourite)
    }
}