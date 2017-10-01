package com.voltek.newsfeed.domain.use_case.news_sources

import com.voltek.newsfeed.domain.repository.NewsSourcesRepository
import com.voltek.newsfeed.domain.use_case.BaseUseCase
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.Result
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

/**
 * Notify presenter, if enabled news sources has changed.
 */
class NewsSourcesUpdatesUseCase(
        private val newsSourcesRepository: NewsSourcesRepository,
        jobScheduler: Scheduler, uiScheduler: Scheduler
)  : BaseUseCase<Unit?, Unit>(jobScheduler, uiScheduler) {

    override fun buildObservable(parameter: Parameter<Unit?>): Observable<Result<Unit?>> =
            newsSourcesRepository
                    .getSourcesEnabledObservable()
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .map { Result(null) }
}
