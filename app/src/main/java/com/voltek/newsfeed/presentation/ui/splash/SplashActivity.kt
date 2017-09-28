package com.voltek.newsfeed.presentation.ui.splash

import android.content.Intent
import com.arellomobile.mvp.presenter.InjectPresenter
import com.voltek.newsfeed.navigation.command.CommandOpenArticlesListScreen
import com.voltek.newsfeed.navigation.command.CommandOpenNewsSourcesScreen
import com.voltek.newsfeed.navigation.proxy.Command
import com.voltek.newsfeed.presentation.base.BaseActivity
import com.voltek.newsfeed.presentation.ui.list.ListActivity
import com.voltek.newsfeed.presentation.ui.news_sources.NewsSourcesActivity

class SplashActivity : BaseActivity(),
        SplashView {

    @InjectPresenter
    lateinit var mPresenter: SplashPresenter

    override fun executeCommand(command: Command): Boolean = when (command) {
        is CommandOpenArticlesListScreen -> {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        is CommandOpenNewsSourcesScreen -> {
            val intent = Intent(this, NewsSourcesActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        else -> false
    }

    override fun attachInputListeners() {}

    override fun detachInputListeners() {}
}
