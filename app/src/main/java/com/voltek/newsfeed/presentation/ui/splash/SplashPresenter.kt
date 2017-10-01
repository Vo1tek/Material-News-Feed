package com.voltek.newsfeed.presentation.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.domain.exception.NoNewsSourcesSelectedException
import com.voltek.newsfeed.presentation.entity.SourceUI
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUseCase
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenArticlesListScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandSystemMessage
import com.voltek.newsfeed.presentation.navigation.proxy.Router
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
    lateinit var mNewsSources: NewsSourcesUseCase

    override fun notify(event: Event) {}

    init {
        NewsApp.presenterComponent.inject(this)

        bind(arrayOf(mNewsSources))

        checkNewsSources()
    }

    private fun checkNewsSources() {
        // Fetch news sources in background.
        // Check, if there is no enabled news sources, open NewsSources screen with proper message.
        mNewsSources.execute(
                Parameter(NewsSourcesUseCase.GET),
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
