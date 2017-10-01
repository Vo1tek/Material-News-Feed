package com.voltek.newsfeed.dagger

import android.content.Context
import com.voltek.newsfeed.data.network.BaseInterceptor
import com.voltek.newsfeed.data.network.ConnectivityInterceptor
import com.voltek.newsfeed.data.network.NewsApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl: String) {

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(BaseInterceptor())
        httpClient.addInterceptor(ConnectivityInterceptor(context))
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}
