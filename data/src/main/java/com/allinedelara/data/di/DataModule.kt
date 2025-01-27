package com.allinedelara.data.di

import com.allinedelara.data.api.Api
import com.allinedelara.data.repo.RemoteRepo
import com.allinedelara.data.repo.RemoteRepoImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}