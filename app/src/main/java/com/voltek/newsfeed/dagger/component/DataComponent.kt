package com.voltek.newsfeed.dagger.component

import com.voltek.newsfeed.dagger.module.AppModule
import com.voltek.newsfeed.data.storage.NewsSourcesStorage
import com.voltek.newsfeed.data.platform.ResourcesManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface DataComponent {

    fun inject(storage: NewsSourcesStorage)

    fun inject(platform: ResourcesManager)
}
