package com.voltek.newsfeed.dagger

import com.voltek.newsfeed.domain.use_case.articles.GetArticlesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.EnableNewsSourceUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUpdatesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUseCase
import com.voltek.newsfeed.presentation.navigation.proxy.Router
import com.voltek.newsfeed.presentation.ui.details.DetailsPresenter
import com.voltek.newsfeed.presentation.ui.list.ListPresenter
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesPresenter
import com.voltek.newsfeed.presentation.ui.splash.SplashPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideSplashPresenter(router: Router, newsSourcesUseCase: NewsSourcesUseCase)
            = SplashPresenter(router, newsSourcesUseCase)

    @Provides
    fun provideNewsSourcesPresenter(
            newsSourcesUseCase: NewsSourcesUseCase,
            enableNewsSourceUseCase: EnableNewsSourceUseCase
    ) = NewsSourcesPresenter(newsSourcesUseCase, enableNewsSourceUseCase)

    @Provides
    fun provideListPresenter(router: Router,
                             getArticlesUseCase: GetArticlesUseCase,
                             newsSourcesUpdatesUseCase: NewsSourcesUpdatesUseCase
    ) = ListPresenter(router, getArticlesUseCase, newsSourcesUpdatesUseCase)

    @Provides
    fun provideDetailsPresenter(router: Router) = DetailsPresenter(router)
}
