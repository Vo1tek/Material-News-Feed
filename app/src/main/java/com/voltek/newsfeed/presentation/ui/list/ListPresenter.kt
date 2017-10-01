package com.voltek.newsfeed.presentation.ui.list

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.articles.GetArticlesUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUpdatesUseCase
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenArticleDetailsScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.presentation.navigation.proxy.Router
import com.voltek.newsfeed.presentation.ui.list.ListContract.ListModel
import com.voltek.newsfeed.presentation.ui.list.ListContract.ListView
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

@InjectViewState
class ListPresenter(
        private val router: Router,
        private val articles: GetArticlesUseCase,
        private val newsSourcesChanges: NewsSourcesUpdatesUseCase
) : BasePresenter<ListView>() {

    // Holds current model through full presenter lifecycle
    private val model: ListModel = ListModel { viewState.render(it as ListModel) }

    init {
        bind(arrayOf(articles, newsSourcesChanges))
        listenForChanges()
        loadArticles()
    }

    // View notify presenter about events using this method
    override fun notify(event: Event) {
        when (event) {
            is Event.OpenArticleDetails -> router.execute(CommandOpenArticleDetailsScreen(event.article))
            is Event.OpenNewsSources -> router.execute(CommandOpenNewsSourcesScreen())
            is Event.Refresh -> {
                if (!model.loading) {
                    loadArticles()
                }
            }
        }
    }

    override fun attachView(view: ListView?) {
        super.attachView(view)
        model.scrollToTop = false
    }

    private fun loadArticles() {
        model.articles.clear()
        model.loading = true
        model.message = ""
        model.update()

        articles.execute(
                Parameter(),
                Consumer {
                    model.addData(it.data)
                    model.message = it.message
                    model.update()
                },
                Consumer {
                    model.message = it.message ?: ""
                    finishLoading()
                },
                Action {
                    finishLoading()
                }
        )
    }

    private fun listenForChanges() {
        // Listen for enabled news sources changes and reload articles when it happens.
        newsSourcesChanges.execute(
                Parameter(),
                Consumer {
                    model.scrollToTop = true
                    loadArticles()
                },
                Consumer {
                    it.printStackTrace()
                },
                Action {}
        )
    }

    private fun finishLoading() {
        model.loading = false
        model.update()
    }
}
