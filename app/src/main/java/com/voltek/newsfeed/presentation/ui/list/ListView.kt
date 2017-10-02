package com.voltek.newsfeed.presentation.ui.list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.voltek.newsfeed.presentation.base.BaseView

interface ListView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(model: ListModel)
}
