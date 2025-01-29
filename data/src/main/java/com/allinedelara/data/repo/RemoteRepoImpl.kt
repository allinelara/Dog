package com.allinedelara.data.repo

import com.allinedelara.data.api.Api
import com.allinedelara.data.api.handleApiResult
import com.allinedelara.data.model.DogResponse
import javax.inject.Inject

class RemoteRepoImpl @Inject constructor(private val api: Api) : RemoteRepo {
    override suspend fun getDog(): Result<DogResponse?> {
        return  api.getDog().handleApiResult()
    }
}
