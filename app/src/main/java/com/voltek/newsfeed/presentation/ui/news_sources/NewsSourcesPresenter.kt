package com.voltek.newsfeed.presentation.ui.news_sources

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.news_sources.EnableNewsSourceUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUseCase
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesContract.NewsSourcesModel
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesContract.NewsSourcesView
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

@InjectViewState
class NewsSourcesPresenter : BasePresenter<NewsSourcesView>() {

    @Inject
    lateinit var mNewsSources: NewsSourcesUseCase

    @Inject
    lateinit var mNewsSourceEnable: EnableNewsSourceUseCase

    private val mModel: NewsSourcesModel =
            NewsSourcesModel { viewState.render(it as NewsSourcesModel) }

    override fun notify(event: Event) {
        when (event) {
            is Event.FilterSources -> {
                mModel.categoryId = event.id
                loadNewsSources(event.filter)
            }
            is Event.Refresh -> {
                mModel.resetId()
                loadNewsSources(NewsSourcesUseCase.REFRESH)
            }
            is Event.EnableNewsSource -> {
                mNewsSourceEnable.execute(
                        Parameter(item = event.source),
                        Consumer {},
                        Consumer {
                            mModel.message = it.message ?: ""
                            mModel.update()
                        },
                        Action {
                            mModel.sources.firstOrNull {
                                it.id == event.source.id
                            }?.isEnabled = !event.source.isEnabled
                        }
                )
            }
        }
    }

    init {
        NewsApp.presenterComponent.inject(this)

        bind(arrayOf(mNewsSources, mNewsSourceEnable))

        loadNewsSources(NewsSourcesUseCase.GET)
    }

    private fun loadNewsSources(filter: String) {
        mModel.sources.clear()
        mModel.loading = true
        mModel.message = ""
        mModel.update()

        mNewsSources.execute(
                Parameter(filter),
                Consumer {
                    mModel.sources = ArrayList(it.data ?: ArrayList())
                    mModel.message = it.message
                },
                Consumer {
                    mModel.message = it.message ?: ""
                    finishLoading()
                },
                Action {
                    finishLoading()
                }
        )
    }

    private fun finishLoading() {
        mModel.loading = false
        mModel.update()
    }
}
