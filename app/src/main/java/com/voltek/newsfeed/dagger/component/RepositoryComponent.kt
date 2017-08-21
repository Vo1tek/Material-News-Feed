package com.voltek.newsfeed.dagger.component

import com.voltek.newsfeed.dagger.module.NetworkModule
import com.voltek.newsfeed.dagger.module.PlatformModule
import com.voltek.newsfeed.dagger.module.StorageModule
import com.voltek.newsfeed.domain.repository.ArticlesRepository
import com.voltek.newsfeed.domain.repository.NewsSourcesRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        StorageModule::class,
        NetworkModule::class,
        PlatformModule::class)
)
interface RepositoryComponent {

    fun inject(repository: ArticlesRepository)

    fun inject(repository: NewsSourcesRepository)
}
