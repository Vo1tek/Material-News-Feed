package com.voltek.newsfeed.presentation.list

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.domain.interactor.Parameter
import com.voltek.newsfeed.domain.interactor.articles.GetArticlesInteractor
import com.voltek.newsfeed.domain.interactor.news_sources.NewsSourcesUpdatesInteractor
import com.voltek.newsfeed.navigation.command.CommandOpenArticleDetailsScreen
import com.voltek.newsfeed.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.navigation.proxy.Router
import com.voltek.newsfeed.presentation.BasePresenter
import com.voltek.newsfeed.presentation.Event
import com.voltek.newsfeed.presentation.list.ListContract.ListModel
import com.voltek.newsfeed.presentation.list.ListContract.ListView
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

        listenForChanges()

        loadArticles()
    }

    override fun attachView(view: ListView?) {
        super.attachView(view)
        viewState.attachInputListeners()
        mModel.scrollToTop = false
    }

    override fun detachView(view: ListView?) {
        viewState.detachInputListeners()
        super.detachView(view)
    }

    override fun onDestroy() {
        mArticles.unsubscribe()
        mNewsSourcesChanges.unsubscribe()
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