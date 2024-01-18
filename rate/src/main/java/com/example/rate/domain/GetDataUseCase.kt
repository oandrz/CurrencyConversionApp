package com.example.rate.domain

import com.example.rate.data.GetDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val repository: GetDataRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.fetchData()
    }
}