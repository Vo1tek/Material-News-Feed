package com.voltek.newsfeed.presentation.ui.news_sources

import com.arellomobile.mvp.InjectViewState
import com.voltek.newsfeed.domain.use_case.Parameter
import com.voltek.newsfeed.domain.use_case.news_sources.EnableNewsSourceUseCase
import com.voltek.newsfeed.domain.use_case.news_sources.NewsSourcesUseCase
import com.voltek.newsfeed.presentation.base.BasePresenter
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesContract.NewsSourcesModel
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesContract.NewsSourcesView
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

@InjectViewState
class NewsSourcesPresenter(
        private val newsSources: NewsSourcesUseCase,
        private val newsSourceEnable: EnableNewsSourceUseCase
) : BasePresenter<NewsSourcesView>() {

    private val model: NewsSourcesModel =
            NewsSourcesModel { viewState.render(it as NewsSourcesModel) }

    init {
        bind(arrayOf(newsSources, newsSourceEnable))

        loadNewsSources(NewsSourcesUseCase.GET)
    }

    override fun notify(event: Event) {
        when (event) {
            is Event.FilterSources -> {
                model.categoryId = event.id
                loadNewsSources(event.filter)
            }
            is Event.Refresh -> {
                model.resetId()
                loadNewsSources(NewsSourcesUseCase.REFRESH)
            }
            is Event.EnableNewsSource -> {
                newsSourceEnable.execute(
                        Parameter(item = event.source),
                        Consumer {},
                        Consumer {
                            model.message = it.message ?: ""
                            model.update()
                        },
                        Action {
                            model.sources.firstOrNull {
                                it.id == event.source.id
                            }?.isEnabled = !event.source.isEnabled
                        }
                )
            }
        }
    }

    private fun loadNewsSources(filter: String) {
        model.sources.clear()
        model.loading = true
        model.message = ""
        model.update()

        newsSources.execute(
                Parameter(filter),
                Consumer {
                    model.sources = ArrayList(it.data ?: ArrayList())
                    model.message = it.message
                },
                Consumer {
                    model.message = it.message ?: ""
                    finishLoading()
                },
                Action {
                    finishLoading()
                }
        )
    }

    private fun finishLoading() {
        model.loading = false
        model.update()
    }
}
