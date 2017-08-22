package com.voltek.newsfeed.dagger.module

import com.voltek.newsfeed.data.platform.ResourcesManager
import dagger.Module
import dagger.Provides

@Module
class PlatformModule {

    @Provides
    fun provideResourcesManager(): ResourcesManager = ResourcesManager()
}
