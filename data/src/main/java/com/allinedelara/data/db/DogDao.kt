package com.allinedelara.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {

    @Query("SELECT * FROM dog")
    fun getAll(): Flow<List<DogEntity>>

    @Insert
    fun insert(image: DogEntity)

    @Delete
    fun delete(image: DogEntity)

    @Query("SELECT * FROM dog WHERE image =:image ")
    fun getDog(image: String): DogEntity

    @Query("DELETE FROM dog WHERE image = :image")
    suspend fun deleteByImage(image: String)
}