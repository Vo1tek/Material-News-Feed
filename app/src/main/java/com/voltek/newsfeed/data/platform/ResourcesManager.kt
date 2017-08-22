package com.voltek.newsfeed.data.platform

import android.content.Context
import android.support.annotation.IdRes
import com.voltek.newsfeed.NewsApp
import javax.inject.Inject

class ResourcesManager {

    @Inject
    lateinit var mContext: Context

    init {
        NewsApp.dataComponent.inject(this)
    }

    fun getString(@IdRes id: Int): String = mContext.getString(id)
}
