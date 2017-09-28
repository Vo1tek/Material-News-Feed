package com.voltek.newsfeed.presentation.base

import com.arellomobile.mvp.MvpPresenter

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

    abstract fun notify(event: Event)
}
