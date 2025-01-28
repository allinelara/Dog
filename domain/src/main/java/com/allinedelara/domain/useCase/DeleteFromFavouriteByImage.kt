package com.allinedelara.domain.useCase

import com.allinedelara.data.repo.DataBaseRepo
import javax.inject.Inject

class DeleteFromFavouriteByImage @Inject constructor(private val dataBaseRepo: DataBaseRepo) {
    suspend operator fun invoke(image: String) {
        return dataBaseRepo.deleteByImage(image = image)
    }
}