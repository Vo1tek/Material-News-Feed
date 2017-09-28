package com.voltek.newsfeed.presentation.ui.news_sources

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.voltek.newsfeed.R
import com.voltek.newsfeed.domain.exception.NoNewsSourcesSelectedException
import com.voltek.newsfeed.navigation.command.CommandSystemMessage
import com.voltek.newsfeed.navigation.proxy.Command
import com.voltek.newsfeed.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_generic.*

class NewsSourcesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic)
        setupToolbar(displayHomeAsUpEnabled = true)

        if (savedInstanceState == null)
            replaceFragment(
                    NewsSourcesFragment.newInstance(),
                    R.id.fragment_container,
                    NewsSourcesFragment.TAG
            )
    }

    override fun executeCommand(command: Command): Boolean = when (command) {
        is CommandSystemMessage -> {
            when (command.error) {
                is NoNewsSourcesSelectedException -> {
                    val snackBar = Snackbar
                            .make(
                                    root,
                                    R.string.error_no_news_sources_selected,
                                    Snackbar.LENGTH_INDEFINITE
                            )
                    snackBar.setAction(android.R.string.ok) { snackBar.dismiss() }
                    snackBar.show()
                }
            }
            true
        }
        else -> false
    }
}
