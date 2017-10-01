package com.voltek.newsfeed.dagger

import com.voltek.newsfeed.presentation.ui.details.DetailsFragment
import com.voltek.newsfeed.presentation.ui.list.ListFragment
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesFragment
import com.voltek.newsfeed.presentation.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NetworkModule::class,
        PlatformModule::class,
        PresenterModule::class,
        RepositoryModule::class,
        RouterModule::class,
        RouterModule::class,
        StorageModule::class,
        UseCaseModule::class)
)
interface AppComponent {

    fun inject(activity: SplashActivity)

    fun inject(fragment: NewsSourcesFragment)
    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailsFragment)
}
