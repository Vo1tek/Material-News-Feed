package com.voltek.newsfeed.presentation.ui.list

import android.content.Intent
import com.voltek.newsfeed.R
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenArticleDetailsScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenWebsite
import com.voltek.newsfeed.presentation.navigation.proxy.Command
import com.voltek.newsfeed.presentation.base.BaseActivity
import com.voltek.newsfeed.presentation.navigation.command.CommandShareArticle
import com.voltek.newsfeed.presentation.ui.details.DetailsActivity
import com.voltek.newsfeed.presentation.ui.details.DetailsFragment
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.parceler.Parcels

class ListActivity : BaseActivity() {

    private var mDualPane = false

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setupToolbar()
        title = getString(com.voltek.newsfeed.R.string.title_list)

        mDualPane = details_fragment_container != null

        if (savedInstanceState == null)
            replaceFragment(
                    ListFragment.newInstance(),
                    R.id.list_fragment_container,
                    ListFragment.TAG
            )
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_list, menu)
        return true
    }

    override fun executeCommand(command: Command): Boolean = when (command) {
        is CommandOpenNewsSourcesScreen -> openNewsSources()
        is CommandOpenArticleDetailsScreen -> openDetails(command.article)
        is CommandShareArticle -> shareArticle(command)
        is CommandOpenWebsite -> openWebsite(command)
        else -> false
    }

    private fun openDetails(article: ArticleUI): Boolean {
        if (mDualPane) {
            replaceFragment(
                    DetailsFragment.newInstance(article),
                    R.id.details_fragment_container,
                    DetailsFragment.TAG
            )
        } else {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsFragment.ARG_ARTICLE, Parcels.wrap(article))
            startActivity(intent)
        }
        return true
    }

    private fun openNewsSources(): Boolean {
        val intent = Intent(this, NewsSourcesActivity::class.java)
        startActivity(intent)
        return true
    }
}
