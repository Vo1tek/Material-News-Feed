package com.voltek.newsfeed.presentation.ui.news_sources

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.voltek.newsfeed.NewsApp
import com.voltek.newsfeed.R
import com.voltek.newsfeed.presentation.base.BaseFragment
import com.voltek.newsfeed.presentation.base.Event
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesContract.NewsSourcesView
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class NewsSourcesFragment : BaseFragment(),
        NewsSourcesView {

    companion object {
        const val TAG = "NewsSourcesFragment"

        private val CATEGORY_ITEMS_IDS = arrayOf(
                R.id.action_all,
                R.id.action_enabled,
                R.id.action_business,
                R.id.action_entertainment,
                R.id.action_gaming,
                R.id.action_general,
                R.id.action_music,
                R.id.action_politics,
                R.id.action_science_and_nature,
                R.id.action_sport,
                R.id.action_technology
        )

        fun newInstance(): NewsSourcesFragment = NewsSourcesFragment()
    }

    init {
        NewsApp.appComponent.inject(this)
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: NewsSourcesPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var adapter: NewsSourcesAdapter

    private var category: Int = R.id.action_all

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter = NewsSourcesAdapter(context, ArrayList())
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view.hasFixedSize()
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = ScaleInAnimationAdapter(adapter)
        recycler_view.itemAnimator = ScaleInLeftAnimator()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_news_sources, menu)
        menu?.findItem(category)?.isChecked = true
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(category)?.isChecked = true
    }

    override fun attachInputListeners() {
        RxToolbar.itemClicks(activity.toolbar)
                .filter { !swipe_container.isRefreshing }
                .subscribe({
                    when (it.itemId) {
                        in CATEGORY_ITEMS_IDS -> {
                            if (!it.isChecked) {
                                presenter.notify(
                                        Event.FilterSources(it.title.toString(), it.itemId)
                                )
                            }
                        }
                        R.id.action_refresh -> presenter.notify(Event.Refresh())
                    }
                })
                .bind()

        adapter.getOnItemClickObservable()
                .subscribe({ presenter.notify(Event.EnableNewsSource(it)) })
                .bind()
    }

    override fun detachInputListeners() {
        resetCompositeDisposable()
    }

    override fun render(model: NewsSourcesContract.NewsSourcesModel) {
        swipe_container.isEnabled = model.loading
        swipe_container.isRefreshing = model.loading

        adapter.replace(model.sources)

        category = model.categoryId
        activity.invalidateOptionsMenu()

        when (model.categoryId) {
            R.id.action_all -> setTitle(R.string.category_all)
            R.id.action_enabled -> setTitle(R.string.category_enabled)
            R.id.action_business -> setTitle(R.string.category_business)
            R.id.action_entertainment -> setTitle(R.string.category_entertainment)
            R.id.action_gaming -> setTitle(R.string.category_gaming)
            R.id.action_general -> setTitle(R.string.category_general)
            R.id.action_music -> setTitle(R.string.category_music)
            R.id.action_politics -> setTitle(R.string.category_politics)
            R.id.action_science_and_nature -> setTitle(R.string.category_science_and_nature)
            R.id.action_sport -> setTitle(R.string.category_sport)
            R.id.action_technology -> setTitle(R.string.category_technology)
        }

        if (!model.message.isEmpty()) {
            if (model.sources.isEmpty()) {
                tv_message.text = model.message
                tv_message.visibility = View.VISIBLE
            } else {
                tv_message.visibility = View.GONE
                tv_message.text = ""
                toast(model.message)
            }
        } else {
            tv_message.visibility = View.GONE
            tv_message.text = ""
        }
    }

    private fun setTitle(@IdRes id: Int) {
        activity.title = getString(id)
    }
}
