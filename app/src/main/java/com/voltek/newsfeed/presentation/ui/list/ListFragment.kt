package com.voltek.newsfeed.presentation.ui.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.R
import com.voltek.newsfeed.presentation.base.BaseFragment
import com.voltek.newsfeed.presentation.base.Event
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ListFragment : BaseFragment(),
        ListView {

    companion object {
        const val TAG = "ListFragment"

        fun newInstance() = ListFragment()
    }

    init {
        NewsApp.appComponent.inject(this)
        setHasOptionsMenu(true)
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ListPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var adapter: ListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter = ListAdapter(context, ArrayList())
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view.hasFixedSize()
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = ScaleInAnimationAdapter(adapter)
        recycler_view.itemAnimator = SlideInLeftAnimator()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_list, menu)
    }

    override fun attachInputListeners() {
        // Toolbar
        RxToolbar.itemClicks(activity.toolbar)
                .subscribe({
                    when (it.itemId) {
                        R.id.action_news_sources -> presenter.notify(Event.OpenNewsSources())
                    }
                })
                .bind()

        // On article click
        adapter.getOnItemClickObservable()
                .distinctUntilChanged() // Skip reopening of details in two pane mode
                .subscribe({ presenter.notify(Event.OpenArticleDetails(it)) })
                .bind()

        // Swipe to refresh
        RxSwipeRefreshLayout.refreshes(swipe_container)
                .subscribe({ presenter.notify(Event.Refresh()) })
                .bind()
    }

    override fun detachInputListeners() {
        resetCompositeDisposable()
    }

    override fun render(model: ListModel) {
        swipe_container.isRefreshing = model.loading

        adapter.replace(model.articles)

        if (!model.message.isEmpty()) {
            if (model.articles.isEmpty()) {
                tv_message.text = model.message
                tv_message.visibility = VISIBLE
            } else {
                tv_message.visibility = GONE
                tv_message.text = ""
                toast(model.message)
            }
        } else {
            tv_message.visibility = GONE
            tv_message.text = ""
        }

        if (model.scrollToTop) {
            recycler_view.smoothScrollToPosition(0)
        }
    }
}
