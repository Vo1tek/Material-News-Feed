package com.voltek.newsfeed.domain.repository

import com.voltek.newsfeed.R
import com.voltek.newsfeed.data.entity.SourceDB
import com.voltek.newsfeed.data.entity.SourceRAW
import com.voltek.newsfeed.data.network.NewsApi
import com.voltek.newsfeed.data.platform.ResourcesManager
import com.voltek.newsfeed.data.storage.NewsSourcesStorage
import com.voltek.newsfeed.domain.Mapper
import com.voltek.newsfeed.domain.exception.NoConnectionException
import com.voltek.newsfeed.domain.use_case.Result
import com.voltek.newsfeed.presentation.entity.SourceUI
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class NewsSourcesRepository(
        private val api: NewsApi,
        private val storage: NewsSourcesStorage,
        private val res: ResourcesManager
) {

    // Notifies subscribers when enabled news sources has changed
    private var mSourcesEnabledSubject: PublishSubject<Unit> = PublishSubject.create()

    fun getSourcesEnabledObservable(): Observable<Unit> = mSourcesEnabledSubject

    fun getAll(): Observable<Result<List<SourceUI>?>> = Observable.create {
        val emitter = it

        val sourcesCache = storage.queryAll()

        if (sourcesCache.isEmpty()) {
            api.fetchSources()
                    .map { it.sources }
                    .map { it.map { Mapper.sourceRAWtoDB(it) } }
                    .subscribe({
                        storage.save(it)
                        emitter.onNext(Result(storage.queryAll().map { Mapper.sourceDBtoUI(it) }))
                    }, {
                        val message: String = when (it) {
                            is NoConnectionException -> res.getString(R.string.error_no_connection)
                            else -> res.getString(R.string.error_request_failed)
                        }
                        emitter.onNext(Result(null, message))
                    })
        } else {
            emitter.onNext(Result(sourcesCache.map { Mapper.sourceDBtoUI(it) }))
        }

        emitter.onComplete()
    }

    fun getCategory(category: String): Single<Result<List<SourceUI>?>> = Single.create {
        val emitter = it

        val query: List<SourceDB>
        var message = ""

        if (category == res.getString(R.string.category_all)) {
            query = storage.queryAll()

            if (query.isEmpty())
                message = res.getString(R.string.error_no_news_sources_loaded)
        } else if (category == res.getString(R.string.category_enabled) || category.isEmpty()) {
            query = storage.queryEnabled()

            if (query.isEmpty())
                message = res.getString(R.string.error_no_news_sources_selected_yet)
        } else {
            query = storage.queryCategory(category.toLowerCase())

            if (query.isEmpty())
                message = res.getString(R.string.error_no_news_sources_for_category)
        }

        emitter.onSuccess(Result(query.map { Mapper.sourceDBtoUI(it) }, message))
    }

    fun refresh(): Single<Result<List<SourceUI>?>> =
            api.fetchSources()
                    .map { it.sources }
                    .doOnSuccess(this::cacheSources)
                    // TODO handle errors
                    /*.onErrorReturn {
                        val message: String = when (it) {
                            is NoConnectionException -> res.getString(R.string.error_no_connection)
                            else -> res.getString(R.string.error_request_failed)
                        }
                        Result(storage.queryAll().map { Mapper.Source(it) }, message)
                    }*/
                    .flatMap {
                        Single.fromCallable { Result(storage.queryAll().map { Mapper.sourceDBtoUI(it) }) }
                    }

    fun update(id: String, isEnabled: Boolean): Completable = Completable.create {
        val emitter = it

        val source = storage.findById(id)

        if (source != null) {
            source.isEnabled = isEnabled
            storage.save(arrayListOf(source))
            mSourcesEnabledSubject.onNext(Unit)
            emitter.onComplete()
        } else {
            emitter.onError(Exception())
        }
    }

    private fun cacheSources(sources: List<SourceRAW>) {
        val current = storage.queryEnabled()
        val new = (sources as ArrayList).map { Mapper.sourceRAWtoDB(it) }

        for (source in new)
            for (enabled in current)
                if (source.id == enabled.id)
                    source.isEnabled = true

        storage.deleteAll()
        storage.save(new)
    }
}
