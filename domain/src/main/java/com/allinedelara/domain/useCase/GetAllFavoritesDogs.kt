package com.allinedelara.domain.useCase

import com.allinedelara.data.repo.DataBaseRepo
import com.allinedelara.domain.model.Dog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllFavoritesDogs @Inject constructor (private val dataBaseRepo: DataBaseRepo) {
    suspend operator fun invoke(): Flow<Result<List<Dog>>> = flow {
        try {
            dataBaseRepo.getAll().collect { quotes ->
                val dogs = quotes.map { Dog(id = it.id, image = it.image) }
                emit(Result.success(dogs))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

