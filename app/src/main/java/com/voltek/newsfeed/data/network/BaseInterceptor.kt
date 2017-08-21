package com.voltek.newsfeed.data.network

import com.voltek.newsfeed.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BaseInterceptor : Interceptor {

    /**
     * Adds api key params to each HTTP request
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url().newBuilder()
                .addQueryParameter(PARAM_API_KEY, BuildConfig.ApiKey)
                .build()
        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}
