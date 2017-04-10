package com.voltek.materialnewsfeed.ui.list

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.net.ConnectivityManager
import android.os.Bundle
import com.voltek.materialnewsfeed.MaterialNewsFeedApp
import com.voltek.materialnewsfeed.data.api.Article
import com.voltek.materialnewsfeed.data.ArticlesRepository
import com.voltek.materialnewsfeed.data.api.NewsApiArticlesResponse
import com.voltek.materialnewsfeed.data.DataSource
import org.parceler.Parcels
import javax.inject.Inject

class ListPresenter(navigator: ListContract.Navigator) : ListContract.Presenter(navigator) {

    init {
        MaterialNewsFeedApp.mainComponent.inject(this)
    }

    @Inject
    lateinit var mContext: Context

    private var mRepository: DataSource.Articles = ArticlesRepository()

    override fun attach(view: ListContract.View, savedInstanceState: Bundle?) {
        super.attach(view, savedInstanceState)

        val listClicksDisposable = mView?.onItemClick()
                ?.subscribe({ article ->
                    mNavigator.openDetails(article)
                }, this::handleError)

        mDisposable.add(listClicksDisposable)
    }

    override fun onFirstLaunch() {
        getArticles()
    }

    override fun onRestore(savedInstanceState: Bundle?) {
        val articles: List<Article> = Parcels.unwrap(savedInstanceState?.getParcelable(ListFragment.BUNDLE_ARTICLES))
        if (articles.isEmpty()) {
            getArticles()
        } else {
            mView?.handleResponse(articles)
        }
    }

    fun getArticles() {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        // Check if internet connection is available
        if (networkInfo != null && networkInfo.isConnected) {
            val observable = mRepository.provideArticles("the-next-web")
            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse, this::handleError)
        } else {
            handleError(NoSuchMethodError())
        }
    }

    fun handleResponse(response: NewsApiArticlesResponse) {
        val articles: List<Article> = response.articles

        if (articles.isEmpty()) {
            mView?.handleError("There is no news")
        } else {
            mView?.handleResponse(articles)
        }
    }

    fun handleError(error: Throwable) {
        error.printStackTrace()
        mView?.handleError(error.javaClass.simpleName)
    }
}
