package com.voltek.newsfeed.presentation.ui.list

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.domain.interactor.Parameter
import com.voltek.newsfeed.domain.interactor.articles.GetArticlesInteractor
import com.voltek.newsfeed.domain.interactor.news_sources.NewsSourcesUpdatesInteractor
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenArticleDetailsScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.presentation.navigation.proxy.Router
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.ui.list.ListContract.ListModel
import com.voltek.newsfeed.presentation.ui.list.ListContract.ListView
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

@InjectViewState
class ListPresenter : BasePresenter<ListView>() {

    @Inject
    lateinit var mRouter: Router

    @Inject
    lateinit var mArticles: GetArticlesInteractor

    @Inject
    lateinit var mNewsSourcesChanges: NewsSourcesUpdatesInteractor

    // Holds current model through full presenter lifecycle
    private val mModel: ListModel = ListModel { viewState.render(it as ListModel) }

    // View notify presenter about events using this method
    override fun notify(event: Event) {
        when (event) {
            is Event.OpenArticleDetails -> mRouter.execute(CommandOpenArticleDetailsScreen(event.article))
            is Event.OpenNewsSources -> mRouter.execute(CommandOpenNewsSourcesScreen())
            is Event.Refresh -> {
                if (!mModel.loading) {
                    loadArticles()
                }
            }
        }
    }

    init {
        NewsApp.presenterComponent.inject(this)

        bind(arrayOf(mArticles, mNewsSourcesChanges))

        listenForChanges()

        loadArticles()
    }

    override fun attachView(view: ListView?) {
        super.attachView(view)
        mModel.scrollToTop = false
    }

    private fun loadArticles() {
        mModel.articles.clear()
        mModel.loading = true
        mModel.message = ""
        mModel.update()

        mArticles.execute(
                Parameter(),
                Consumer {
                    mModel.addData(it.data)
                    mModel.message = it.message
                    mModel.update()
                },
                Consumer {
                    mModel.message = it.message ?: ""
                    finishLoading()
                },
                Action {
                    finishLoading()
                }
        )
    }

    private fun listenForChanges() {
        // Listen for enabled news sources changes and reload articles when it happens.
        mNewsSourcesChanges.execute(
                Parameter(),
                Consumer {
                    mModel.scrollToTop = true
                    loadArticles()
                },
                Consumer {
                    it.printStackTrace()
                },
                Action {}
        )
    }

    private fun finishLoading() {
        mModel.loading = false
        mModel.update()
    }
}
