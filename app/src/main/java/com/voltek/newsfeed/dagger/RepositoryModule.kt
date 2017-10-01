package com.voltek.newsfeed.dagger

import com.voltek.newsfeed.data.network.NewsApi
import com.voltek.newsfeed.data.platform.ResourcesManager
import com.voltek.newsfeed.data.storage.NewsSourcesStorage
import com.voltek.newsfeed.domain.repository.ArticlesRepository
import com.voltek.newsfeed.domain.repository.NewsSourcesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun provideArticlesRepository(newsApi: NewsApi, resourcesManager: ResourcesManager)
            = ArticlesRepository(newsApi, resourcesManager)

    @Provides
    @Singleton
    fun provideNewsSourcesRepository(
            newsApi: NewsApi,
            resourcesManager: ResourcesManager,
            newsSourcesStorage: NewsSourcesStorage
    ) = NewsSourcesRepository(newsApi, newsSourcesStorage, resourcesManager)
}
