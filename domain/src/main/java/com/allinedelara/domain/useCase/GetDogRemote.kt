package com.allinedelara.domain.useCase

import com.allinedelara.data.repo.RemoteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDogRemote @Inject constructor(private val remoteRepo: RemoteRepo) {
     operator fun invoke(): Flow<Result<String?>> {
         return flow {
             remoteRepo.getDog().mapCatching { it?.message }.let { emit(it) }
         }
    }
}