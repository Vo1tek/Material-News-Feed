package com.voltek.newsfeed.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.voltek.newsfeed.domain.use_case.BaseUseCase

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

    private val interactors = ArrayList<BaseUseCase<*, *>>()

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

    protected fun bind(useCases: Array<BaseUseCase<*, *>>) =
            this.interactors.addAll(useCases)
}
