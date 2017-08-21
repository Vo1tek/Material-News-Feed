package com.voltek.newsfeed.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.voltek.newsfeed.domain.exception.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    /**
     * Check if device has internet connection and throw corresponding exception
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isNotOnline())
            throw NoConnectionException()

        val request = chain.request()
        return chain.proceed(request)
    }

    private fun isNotOnline(): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return !(netInfo != null && netInfo.isConnected)
    }
}
