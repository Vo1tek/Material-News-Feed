package com.voltek.newsfeed.dagger.module

import com.voltek.newsfeed.domain.use_case.articles.GetArticlesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.EnableNewsSourceUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUpdatesUseCase
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class InteractorModule(
        val uiScheduler: Scheduler,
        val ioScheduler: Scheduler,
        val computationScheduler: Scheduler
) {

    @Provides
    fun provideGetArticlesInteractor(): GetArticlesUseCase =
            GetArticlesUseCase(ioScheduler, uiScheduler)

    @Provides
    fun provideNewsSourcesInteractor(): NewsSourcesUseCase =
            NewsSourcesUseCase(ioScheduler, uiScheduler)

    @Provides
    fun provideEnableNewsSourceInteractor(): EnableNewsSourceUseCase =
            EnableNewsSourceUseCase(computationScheduler, uiScheduler)

    @Provides
    @Singleton
    fun provideNewsSourcesUpdatesInteractor(): NewsSourcesUpdatesUseCase =
            NewsSourcesUpdatesUseCase(computationScheduler, uiScheduler)
}
