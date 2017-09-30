package com.voltek.newsfeed.presentation.ui.details

import android.os.Bundle
import com.voltek.newsfeed.R
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.navigation.command.CommandOpenWebsite
import com.voltek.newsfeed.presentation.navigation.command.CommandShareArticle
import com.voltek.newsfeed.presentation.navigation.proxy.Command
import com.voltek.newsfeed.presentation.base.BaseActivity
import org.parceler.Parcels

class DetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic)
        setupToolbar(displayHomeAsUpEnabled = true)

        val article: ArticleUI = Parcels.unwrap(intent.getParcelableExtra(DetailsFragment.ARG_ARTICLE))
        title = article.source

        if (savedInstanceState == null)
            replaceFragment(
                    DetailsFragment.newInstance(article),
                    R.id.fragment_container,
                    DetailsFragment.TAG
            )
    }

    override fun executeCommand(command: Command): Boolean = when (command) {
        is CommandShareArticle -> shareArticle(command)
        is CommandOpenWebsite -> openWebsite(command)
        else -> false
    }
}
