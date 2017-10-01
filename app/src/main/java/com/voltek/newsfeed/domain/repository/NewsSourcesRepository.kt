package com.voltek.newsfeed.domain.repository

import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.R
import com.voltek.newsfeed.data.entity.SourceRAW
import com.voltek.newsfeed.data.network.NewsApi
import com.voltek.newsfeed.data.platform.ResourcesManager
import com.voltek.newsfeed.data.storage.NewsSourcesStorage
import com.voltek.newsfeed.domain.exception.NoConnectionException
import com.voltek.newsfeed.domain.Mapper
import com.voltek.newsfeed.presentation.entity.SourceUI
import com.voltek.newsfeed.domain.use_case.Result
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewsSourcesRepository {

    @Inject
    lateinit var mApi: NewsApi

    @Inject
    lateinit var mDb: NewsSourcesStorage

    @Inject
    lateinit var mRes: ResourcesManager

    // Notifies subscribers when enabled news sources has changed
    private var mSourcesEnabledSubject: PublishSubject<Unit> = PublishSubject.create()

    fun getSourcesEnabledObservable(): Observable<Unit> = mSourcesEnabledSubject

    init {
        NewsApp.repositoryComponent.inject(this)
    }

    fun getAll(): Observable<Result<List<SourceUI>?>> = Observable.create {
        val emitter = it

        val sourcesCache = mDb.queryAll()

        if (sourcesCache.isEmpty()) {
            mApi.fetchSources()
                    .subscribe({
                        mDb.save(it.sources)
                        emitter.onNext(Result(mDb.queryAll().map { Mapper.Source(it) }))
                    }, {
                        val message: String = when (it) {
                            is NoConnectionException -> mRes.getString(R.string.error_no_connection)
                            else -> mRes.getString(R.string.error_request_failed)
                        }
                        emitter.onNext(Result(null, message))
                    })
        } else {
            emitter.onNext(Result(sourcesCache.map { Mapper.Source(it) }))
        }

        emitter.onComplete()
    }

    fun getCategory(category: String): Single<Result<List<SourceUI>?>> = Single.create {
        val emitter = it

        val query: List<SourceRAW>
        var message = ""

        if (category == mRes.getString(R.string.category_all)) {
            query = mDb.queryAll()

            if (query.isEmpty())
                message = mRes.getString(R.string.error_no_news_sources_loaded)
        } else if (category == mRes.getString(R.string.category_enabled) || category.isEmpty()) {
            query = mDb.queryEnabled()

            if (query.isEmpty())
                message = mRes.getString(R.string.error_no_news_sources_selected_yet)
        } else {
            query = mDb.queryCategory(category.toLowerCase())

            if (query.isEmpty())
                message = mRes.getString(R.string.error_no_news_sources_for_category)
        }

        emitter.onSuccess(Result(query.map { Mapper.Source(it) }, message))
    }

    fun refresh(): Single<Result<List<SourceUI>?>> =
            mApi.fetchSources()
                    .map { it.sources }
                    .doOnSuccess(this::cacheSources)
                    // TODO handle errors
                    /*.onErrorReturn {
                        val message: String = when (it) {
                            is NoConnectionException -> mRes.getString(R.string.error_no_connection)
                            else -> mRes.getString(R.string.error_request_failed)
                        }
                        Result(mDb.queryAll().map { Mapper.Source(it) }, message)
                    }*/
                    .flatMap {
                        Single.fromCallable { Result(mDb.queryAll().map { Mapper.Source(it) }) }
                    }

    fun update(id: String, isEnabled: Boolean): Completable = Completable.create {
        val emitter = it

        val source = mDb.findById(id)

        if (source != null) {
            source.isEnabled = isEnabled
            mDb.save(arrayListOf(source))
            mSourcesEnabledSubject.onNext(Unit)
            emitter.onComplete()
        } else {
            emitter.onError(Exception())
        }
    }

    private fun cacheSources(sources: List<SourceRAW>) {
        val current = mDb.queryEnabled()
        val new = sources as ArrayList

        for (source in new)
            for (enabled in current)
                if (source.id == enabled.id)
                    source.isEnabled = true

        mDb.deleteAll()
        mDb.save(new)
    }
}
