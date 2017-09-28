package com.voltek.newsfeed.presentation.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {

    /**
     * Subscribe to view input events in presenter's attachView method
     */
    @StateStrategyType(SkipStrategy::class)
    fun attachInputListeners()

    /**
     * Unsubscribe presenter from input events in detachView method (before call to super)
     */
    @StateStrategyType(SkipStrategy::class)
    fun detachInputListeners()
}
