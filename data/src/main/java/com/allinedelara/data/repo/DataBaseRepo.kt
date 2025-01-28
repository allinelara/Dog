package com.allinedelara.data.repo

import com.allinedelara.data.db.DogEntity
import kotlinx.coroutines.flow.Flow

interface DataBaseRepo {
    suspend fun getAll(): Flow<List<DogEntity>>

    suspend fun addToDataBase(dog: DogEntity)

    suspend fun delete(dog: DogEntity)

    suspend fun deleteByImage(image: String)

    suspend fun getDog(image: String): DogEntity?

}