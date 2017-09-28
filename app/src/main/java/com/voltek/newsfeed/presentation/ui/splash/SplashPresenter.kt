package com.voltek.newsfeed.presentation.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.domain.exception.NoNewsSourcesSelectedException
import com.voltek.newsfeed.presentation.entity.SourceUI
import com.voltek.newsfeed.domain.interactor.Parameter
import com.voltek.newsfeed.domain.interactor.news_sources.NewsSourcesInteractor
import com.voltek.newsfeed.navigation.command.CommandOpenArticlesListScreen
import com.voltek.newsfeed.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.navigation.command.CommandSystemMessage
import com.voltek.newsfeed.navigation.proxy.Router
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

@InjectViewState
class SplashPresenter : BasePresenter<SplashView>() {

    @Inject
    lateinit var mRouter: Router

    @Inject
    lateinit var mNewsSources: NewsSourcesInteractor

    override fun notify(event: Event) {}

    init {
        NewsApp.presenterComponent.inject(this)

        checkNewsSources()
    }

    override fun onDestroy() {
        mNewsSources.unsubscribe()
    }

    private fun checkNewsSources() {
        // Fetch news sources in background.
        // Check, if there is no enabled news sources, open NewsSources screen with proper message.
        mNewsSources.execute(
                Parameter(NewsSourcesInteractor.GET),
                Consumer {
                    result(hasEnabled(it?.data ?: ArrayList()))
                },
                Consumer {
                    it.printStackTrace()
                    result(false)
                },
                Action {}
        )
    }

    private fun hasEnabled(sources: List<SourceUI>): Boolean = sources.any { it.isEnabled }

    private fun result(hasEnabled: Boolean) {
        mRouter.execute(CommandOpenArticlesListScreen())

        if (!hasEnabled) {
            mRouter.execute(CommandOpenNewsSourcesScreen())
            mRouter.execute(CommandSystemMessage(error = NoNewsSourcesSelectedException()))
        }
    }
}
