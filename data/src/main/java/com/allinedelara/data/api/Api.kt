package com.allinedelara.data.api

import com.allinedelara.data.model.Dog
import retrofit2.http.GET

interface Api {
    @GET("random")
    suspend fun getDog(
    ): Dog
}