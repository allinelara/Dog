package com.allinedelara.data.di

import android.content.Context
import androidx.room.Room
import com.allinedelara.data.api.Api
import com.allinedelara.data.db.DogDao
import com.allinedelara.data.db.DogDatabase
import com.allinedelara.data.repo.DataBaseRepo
import com.allinedelara.data.repo.DataBaseRepoImpl
import com.allinedelara.data.repo.RemoteRepo
import com.allinedelara.data.repo.RemoteRepoImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Ensure it's scoped to SingletonComponent
object DataModule {

    @Provides
    @Singleton
    fun provideRemoteRepoImpl(api: Api): RemoteRepo {
        return RemoteRepoImpl(api)
    }

    @Provides
    @Singleton
    fun provideDataBaseRepoImpl(dao: DogDao): DataBaseRepo {
        return DataBaseRepoImpl(dao)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideBaseUrl(): String = "https://dog.ceo/api/breeds/image/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): DogDatabase {
        return Room.databaseBuilder(
            context,
            DogDatabase::class.java,
            name = "dog"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideQuoteDao(database: DogDatabase): DogDao {
        return database.dogDao()
    }

}