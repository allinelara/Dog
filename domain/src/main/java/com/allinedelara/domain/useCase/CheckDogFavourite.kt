package com.allinedelara.domain.useCase

import com.allinedelara.data.repo.DataBaseRepo
import javax.inject.Inject

class CheckDogFavourite @Inject constructor  (private val dataBaseRepo: DataBaseRepo) {
    suspend operator fun invoke(image: String): Boolean {
        return dataBaseRepo.getDog(image) != null
    }
}