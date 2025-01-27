package com.allinedelara.domain.useCase

import com.allinedelara.data.repo.RemoteRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDog @Inject constructor (private val remoteRepo: RemoteRepo) {
    suspend operator fun invoke(): Flow<String> {
        return remoteRepo.getDog()
    }
}