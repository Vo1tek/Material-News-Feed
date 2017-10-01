package com.voltek.newsfeed.dagger.component

import com.voltek.newsfeed.dagger.module.RepositoryModule
import com.voltek.newsfeed.domain.use_case.articles.GetArticlesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.EnableNewsSourceUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUpdatesUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        RepositoryModule::class)
)
interface InteractorComponent {

    fun inject(interactor: GetArticlesUseCase)

    fun inject(interactor: NewsSourcesUseCase)

    fun inject(interactor: NewsSourcesUpdatesUseCase)

    fun inject(interactor: EnableNewsSourceUseCase)
}
