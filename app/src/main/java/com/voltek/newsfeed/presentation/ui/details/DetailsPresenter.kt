package com.voltek.newsfeed.presentation.ui.details

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.navigation.command.CommandBack
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenWebsite
import com.voltek.newsfeed.presentation.navigation.command.CommandShareArticle
import com.voltek.newsfeed.presentation.navigation.proxy.Router
import com.voltek.newsfeed.presentation.ui.details.DetailsContract.DetailsModel
import com.voltek.newsfeed.presentation.ui.details.DetailsContract.DetailsView

@InjectViewState
class DetailsPresenter(private val router: Router) : BasePresenter<DetailsView>() {

    private val model: DetailsModel = DetailsModel { viewState.render(it as DetailsModel) }

    lateinit private var article: ArticleUI

    override fun notify(event: Event) {
        when (event) {
            is Event.Share -> {
                if (!article.isEmpty()) {
                    router.execute(CommandShareArticle(article.title ?: "", article.url ?: ""))
                }
            }
            is Event.OpenWebsite -> router.execute(CommandOpenWebsite(article.url ?: ""))
            is Event.OpenNewsSources -> router.execute(CommandOpenNewsSourcesScreen())
            is Event.Back -> router.execute(CommandBack())
        }
    }

    fun setArticle(articleUI: ArticleUI) {
        article = articleUI

        if (article.isEmpty()) {
            model.articleLoaded = false
            model.update()
        } else {
            model.articleLoaded = true
            model.description = article.description ?: ""
            model.title = article.title ?: ""
            model.urlToImage = article.urlToImage ?: ""
            model.source = article.source
            model.update()
        }
    }
}