package com.voltek.newsfeed.dagger.module

import com.voltek.newsfeed.data.storage.NewsSourcesStorage
import dagger.Module
import dagger.Provides

@Module
class StorageModule {

    @Provides
    fun provideNewsSourcesStorage(): NewsSourcesStorage = NewsSourcesStorage()
}
