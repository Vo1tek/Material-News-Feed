package com.voltek.newsfeed.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.voltek.newsfeed.domain.use_case.BaseUseCase

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

    private val useCases = ArrayList<BaseUseCase<*, *>>()

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
        for (useCase in useCases)
            useCase.unsubscribe()
    }

    protected fun bind(useCases: Array<BaseUseCase<*, *>>) =
            this.useCases.addAll(useCases)
}
