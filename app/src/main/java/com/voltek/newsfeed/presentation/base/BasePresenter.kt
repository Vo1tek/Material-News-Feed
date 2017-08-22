package com.voltek.newsfeed.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.voltek.newsfeed.domain.interactor.BaseInteractor

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

    private val interactors = ArrayList<BaseInteractor<*, *>>()

    abstract fun notify(event: Event)

    override fun attachView(view: View?) {
        super.attachView(view)
        viewState.attachInputListeners()
    }

    override fun detachView(view: View?) {
        viewState.detachInputListeners()
        super.detachView(view)
    }

    override fun onDestroy() {
        for (interactor in interactors)
            interactor.unsubscribe()
    }

    protected fun bind(interactors: Array<BaseInteractor<*, *>>) =
            this.interactors.addAll(interactors)
}
