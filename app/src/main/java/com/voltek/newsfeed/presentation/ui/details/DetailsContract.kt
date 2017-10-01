package com.voltek.newsfeed.presentation.ui.details

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.voltek.newsfeed.presentation.base.BaseModel
import com.voltek.newsfeed.presentation.base.BaseView

object DetailsContract {

    class DetailsModel(subscriber: (BaseModel) -> Unit) : BaseModel(subscriber) {

        var articleLoaded = false

        var title: String = ""
        var description: String = ""
        var urlToImage: String = ""
        var source: String = ""
    }

    interface DetailsView : BaseView {

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun render(model: DetailsModel)
    }
}
