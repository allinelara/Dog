package com.allinedelara.data.repo

import com.allinedelara.data.model.DogResponse

interface RemoteRepo {
    suspend fun getDog(): Result<DogResponse?>
}