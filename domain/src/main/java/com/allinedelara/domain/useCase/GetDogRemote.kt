package com.allinedelara.domain.useCase

import com.allinedelara.data.repo.RemoteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetDogRemote @Inject constructor(private val remoteRepo: RemoteRepo) {
    suspend operator fun invoke(): Flow<Result<String?>> {
        return flowOf(remoteRepo.getDog())
    }
}