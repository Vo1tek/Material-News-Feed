package com.voltek.newsfeed.dagger.module

import com.voltek.newsfeed.presentation.navigation.RouterHolder
import com.voltek.newsfeed.presentation.navigation.proxy.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RouterModule(val routerHolder: RouterHolder) {

    @Provides
    @Singleton
    fun provideRouter(): Router = routerHolder
}
