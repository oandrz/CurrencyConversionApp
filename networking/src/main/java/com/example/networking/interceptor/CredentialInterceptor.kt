package com.example.networking.interceptor

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class CredentialInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header(APP_ID_KEY, APP_ID)
                .build()
        )
    }

    companion object {
        private const val APP_ID = "Token 40c3c8105da3453fbc9b207e1a1e0678"
        private const val APP_ID_KEY = "Authorization"
    }
}