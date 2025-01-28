package com.allinedelara.data.repo

import com.allinedelara.data.db.DogDao
import com.allinedelara.data.db.DogEntity
import kotlinx.coroutines.flow.Flow

class DataBaseRepoImpl(private val dao: DogDao) : DataBaseRepo {

    override suspend fun getAll(): Flow<List<DogEntity>> = dao.getAll()

    override suspend fun addToDataBase(dog: DogEntity) = dao.insert(image = dog)

    override suspend fun delete(dog: DogEntity) = dao.delete(image = dog)

    override suspend fun deleteByImage(image: String)  = dao.deleteByImage(image = image)

    override suspend fun getDog(image: String): DogEntity?  = dao.getDog(image = image)

}