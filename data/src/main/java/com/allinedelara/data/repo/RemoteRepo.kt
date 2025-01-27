package com.allinedelara.data.repo


interface RemoteRepo {
    suspend fun getDog(): Result<String?>
}