package com.voltek.newsfeed.domain.use_case.news_sources

import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.presentation.entity.SourceUI
import com.voltek.newsfeed.domain.use_case.BaseUseCase
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.Result
import com.voltek.newsfeed.domain.repository.NewsSourcesRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

class EnableNewsSourceUseCase(jobScheduler: Scheduler, uiScheduler: Scheduler)
    : BaseUseCase<Unit, SourceUI>(jobScheduler, uiScheduler) {

    @Inject
    lateinit var mNewsSourcesRepo: NewsSourcesRepository

    init {
        NewsApp.interactorComponent.inject(this)
    }

    override fun buildObservable(parameter: Parameter<SourceUI?>): Observable<Result<Unit>> {
        if (parameter.item != null) {
            val source = parameter.item
            return mNewsSourcesRepo
                    .update(source.id, !source.isEnabled)
                    .toObservable<Result<Unit>>()
                    .map { Result(null) }
        } else {
            return Observable.error { NullPointerException() }
        }
    }
}
