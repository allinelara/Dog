package com.allinedelara.data.repo

import com.allinedelara.data.api.Api
import javax.inject.Inject

class RemoteRepoImpl @Inject constructor(private val api: Api) : RemoteRepo {
    override suspend fun getDog(): Result<String?> {
        return try {
            val result = api.getDog()
            if (result.isSuccessful) {
                Result.success(result.body()?.message)
            } else {
                Result.failure(Exception("API error: ${result.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}", e))
        }
    }
}
