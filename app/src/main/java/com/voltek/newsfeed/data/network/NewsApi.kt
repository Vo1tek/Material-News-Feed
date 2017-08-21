package com.voltek.newsfeed.data.network

import com.voltek.newsfeed.data.network.entity.NewsApiArticlesResponse
import com.voltek.newsfeed.data.network.entity.NewsApiSourcesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(ENDPOINT_ARTICLES)
    fun fetchArticles(
            @Query(PARAM_SOURCE) source: String
    ): Single<NewsApiArticlesResponse>

    @GET(ENDPOINT_SOURCES)
    fun fetchSources(): Single<NewsApiSourcesResponse>
}
