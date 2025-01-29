package com.allinedelara.data.api

import retrofit2.Response

fun <T> Response<T>.handleApiResult(): Result<T> {
    return try {
        val response = this
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Response body is null"))
            }
        } else {
            Result.failure(Exception("API error: ${response.code()} - ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(Exception("Network error: ${e.message}", e))
    }
}