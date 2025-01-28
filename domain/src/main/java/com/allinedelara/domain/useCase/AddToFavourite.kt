package com.allinedelara.domain.useCase

import com.allinedelara.data.db.DogEntity
import com.allinedelara.data.repo.DataBaseRepo
import javax.inject.Inject

class AddToFavourite @Inject constructor(private val dataBaseRepo: DataBaseRepo) {
    suspend operator fun invoke(dogImage: String) {
        return dataBaseRepo.addToDataBase(DogEntity(id = 0, image = dogImage))
    }
}