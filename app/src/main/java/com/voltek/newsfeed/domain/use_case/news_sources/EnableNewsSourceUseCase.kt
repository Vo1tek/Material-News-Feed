package com.voltek.newsfeed.domain.use_case.news_sources

import com.voltek.newsfeed.domain.repository.NewsSourcesRepository
import com.voltek.newsfeed.domain.use_case.BaseUseCase
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.Result
import com.voltek.newsfeed.presentation.entity.SourceUI
import io.reactivex.Observable
import io.reactivex.Scheduler

class EnableNewsSourceUseCase(
        private val newsSourcesRepository: NewsSourcesRepository,
        jobScheduler: Scheduler, uiScheduler: Scheduler
) : BaseUseCase<Unit, SourceUI>(jobScheduler, uiScheduler) {

    override fun buildObservable(parameter: Parameter<SourceUI?>): Observable<Result<Unit>> {
        if (parameter.item != null) {
            val source = parameter.item
            return newsSourcesRepository
                    .update(source.id, !source.isEnabled)
                    .toObservable<Result<Unit>>()
                    .map { Result(null) }
        } else {
            return Observable.error { NullPointerException() }
        }
    }
}
