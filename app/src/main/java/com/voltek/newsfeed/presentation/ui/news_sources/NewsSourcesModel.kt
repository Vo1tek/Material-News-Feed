package com.voltek.newsfeed.presentation.ui.news_sources

import com.voltek.newsfeed.R
import com.voltek.newsfeed.presentation.base.BaseModel
import com.voltek.newsfeed.presentation.entity.SourceUI

class NewsSourcesModel(subscriber: (BaseModel) -> Unit) : BaseModel(subscriber) {

    var categoryId: Int = R.id.action_all

    var loading: Boolean = false

    var sources: ArrayList<SourceUI> = ArrayList()

    var message: String = ""

    fun resetId() {
        categoryId = R.id.action_all
    }
}
