package com.voltek.newsfeed.presentation.ui.news_sources

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.voltek.newsfeed.presentation.base.BaseView

interface NewsSourcesView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(model: NewsSourcesModel)
}
