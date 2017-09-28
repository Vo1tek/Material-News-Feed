package com.voltek.newsfeed.presentation.ui.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.voltek.newsfeed.R
import com.voltek.newsfeed.presentation.base.BaseFragment
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.ui.list.ListContract.ListModel
import com.voltek.newsfeed.presentation.ui.list.ListContract.ListView
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar.*

class ListFragment : BaseFragment(),
        ListView {

    companion object {
        const val TAG = "ListFragment"

        fun newInstance() = ListFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: ListPresenter

    private lateinit var mAdapter: ListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mAdapter = ListAdapter(context, ArrayList())
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view.hasFixedSize()
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = ScaleInAnimationAdapter(mAdapter)
        recycler_view.itemAnimator = SlideInLeftAnimator()
    }

    override fun attachInputListeners() {
        // Toolbar
        RxToolbar.itemClicks(activity.toolbar)
                .subscribe({
                    when (it.itemId) {
                        R.id.action_news_sources -> mPresenter.notify(Event.OpenNewsSources())
                    }
                })
                .bind()

        // On article click
        mAdapter.getOnItemClickObservable()
                .distinctUntilChanged() // Skip reopening of details in two pane mode
                .subscribe({ mPresenter.notify(Event.OpenArticleDetails(it)) })
                .bind()

        // Swipe to refresh
        RxSwipeRefreshLayout.refreshes(swipe_container)
                .subscribe({ mPresenter.notify(Event.Refresh()) })
                .bind()
    }

    override fun detachInputListeners() {
        resetCompositeDisposable()
    }

    override fun render(model: ListModel) {
        swipe_container.isRefreshing = model.loading

        mAdapter.replace(model.articles)

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
