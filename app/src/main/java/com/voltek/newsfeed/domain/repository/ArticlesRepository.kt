package com.voltek.newsfeed.domain.repository

import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.R
import com.voltek.newsfeed.data.network.NewsApi
import com.voltek.newsfeed.data.platform.ResourcesManager
import com.voltek.newsfeed.domain.Mapper
import com.voltek.newsfeed.domain.exception.NoConnectionException
import com.voltek.newsfeed.domain.use_case.Result
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.entity.SourceUI
import io.reactivex.Observable
import javax.inject.Inject

class ArticlesRepository {

    @Inject
    lateinit var mApi: NewsApi

    @Inject
    lateinit var mRes: ResourcesManager

    init {
        NewsApp.repositoryComponent.inject(this)
    }

    fun get(sources: List<SourceUI>): Observable<Result<List<ArticleUI>?>> = Observable.create {
        val emitter = it

        if (!sources.isEmpty()) {
            for (source in sources) {
                mApi.fetchArticles(source.id)
                        .subscribe({
                            val result = ArrayList<ArticleUI>()
                            val sourceTitle = ArticleUI()
                            result.add(sourceTitle)
                            result.addAll(it.articles.map { Mapper.Article(it) })
                            result.forEach { article -> article.source = source.name }
                            emitter.onNext(Result(result))
                        }, {
                            val message: String = when (it) {
                                is NoConnectionException -> mRes.getString(R.string.error_no_connection)
                                else -> mRes.getString(R.string.error_retrieve_failed) + source.name
                            }
                            emitter.onNext(Result(null, message))
                        })
            }
        } else {
            emitter.onError(Exception(mRes.getString(R.string.error_no_news_sources_selected)))
        }

        emitter.onComplete()
    }
}
