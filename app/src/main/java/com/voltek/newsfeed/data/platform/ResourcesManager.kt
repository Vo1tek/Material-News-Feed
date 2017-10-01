package com.voltek.newsfeed.data.platform

import android.content.Context
import android.support.annotation.IdRes

class ResourcesManager(private val context: Context) {

    fun getString(@IdRes id: Int): String = context.getString(id)
}
