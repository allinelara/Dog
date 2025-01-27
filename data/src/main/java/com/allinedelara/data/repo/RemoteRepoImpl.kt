package com.allinedelara.data.repo

import com.allinedelara.data.api.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RemoteRepoImpl @Inject constructor(private val api: Api) : RemoteRepo {
    override suspend fun getDog(): Flow<String> =
        flowOf(api.getDog().message)
}
