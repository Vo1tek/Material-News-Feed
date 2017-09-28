package com.voltek.newsfeed.presentation.ui.list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.base.BaseModel
import com.voltek.newsfeed.presentation.base.BaseView

object ListContract {

    class ListModel(subscriber: (BaseModel) -> Unit) : BaseModel(subscriber) {

        var scrollToTop: Boolean = false

        var loading: Boolean = false

        var articles: ArrayList<ArticleUI> = ArrayList()

        var message: String = ""

        fun addData(data: List<ArticleUI>?) {
            if (data != null && !data.isEmpty())
                articles.addAll(data)
        }
    }

    interface ListView : BaseView {

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun render(model: ListModel)
    }
}
