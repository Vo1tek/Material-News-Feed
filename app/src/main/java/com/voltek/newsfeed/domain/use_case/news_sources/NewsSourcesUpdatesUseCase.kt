package com.voltek.newsfeed.domain.use_case.news_sources

import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.domain.use_case.BaseUseCase
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.Result
import com.voltek.newsfeed.domain.repository.NewsSourcesRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Notify presenter, if enabled news sources has changed.
 */
class NewsSourcesUpdatesUseCase(jobScheduler: Scheduler, uiScheduler: Scheduler)
    : BaseUseCase<Unit?, Unit>(jobScheduler, uiScheduler) {

    @Inject
    lateinit var mNewsSourcesRepo: NewsSourcesRepository

    init {
        NewsApp.interactorComponent.inject(this)
    }

    override fun buildObservable(parameter: Parameter<Unit?>): Observable<Result<Unit?>> =
            mNewsSourcesRepo
                    .getSourcesEnabledObservable()
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .map { Result(null) }
}
