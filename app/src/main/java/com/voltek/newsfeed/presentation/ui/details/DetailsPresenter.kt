package com.voltek.newsfeed.presentation.ui.details

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenWebsite
import com.voltek.newsfeed.presentation.navigation.command.CommandShareArticle
import com.voltek.newsfeed.presentation.navigation.proxy.Router
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.navigation.command.CommandBack
import com.voltek.newsfeed.presentation.ui.details.DetailsContract.DetailsModel
import com.voltek.newsfeed.presentation.ui.details.DetailsContract.DetailsView
import javax.inject.Inject

@InjectViewState
class DetailsPresenter(private val article: ArticleUI) : BasePresenter<DetailsView>() {

    @Inject
    lateinit var mRouter: Router

    private val mModel: DetailsModel = DetailsModel { viewState.render(it as DetailsModel) }

    override fun notify(event: Event) {
        when (event) {
            is Event.Share -> {
                if (!article.isEmpty()) {
                    mRouter.execute(CommandShareArticle(article.title ?: "", article.url ?: ""))
                }
            }
            is Event.OpenWebsite -> mRouter.execute(CommandOpenWebsite(article.url ?: ""))
            is Event.OpenNewsSources -> mRouter.execute(CommandOpenNewsSourcesScreen())
            is Event.Back -> mRouter.execute(CommandBack())
        }
    }

    init {
        NewsApp.presenterComponent.inject(this)

        if (article.isEmpty()) {
            mModel.articleLoaded = false
            mModel.update()
        } else {
            mModel.articleLoaded = true
            mModel.description = article.description ?: ""
            mModel.title = article.title ?: ""
            mModel.urlToImage = article.urlToImage ?: ""
            mModel.source = article.source
            mModel.update()
        }
    }
}
