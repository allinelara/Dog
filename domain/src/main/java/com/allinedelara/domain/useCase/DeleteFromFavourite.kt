package com.allinedelara.domain.useCase

import com.allinedelara.data.db.DogEntity
import com.allinedelara.data.repo.DataBaseRepo
import com.allinedelara.domain.model.Dog
import javax.inject.Inject

class DeleteFromFavourite @Inject constructor (private val dataBaseRepo: DataBaseRepo) {
    suspend operator fun invoke(dog: Dog) {
        return dataBaseRepo.delete(DogEntity(id = dog.id, image = dog.image))
    }
}