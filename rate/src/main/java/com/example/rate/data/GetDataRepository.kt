package com.example.rate.data

import javax.inject.Inject

class GetDataRepository @Inject constructor(
    private val service: DataService
) {

    suspend fun fetchData() = service.getRates()
}