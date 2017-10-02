package com.voltek.newsfeed.presentation.ui.details

import com.voltek.newsfeed.presentation.base.BaseModel

class DetailsModel(subscriber: (BaseModel) -> Unit) : BaseModel(subscriber) {

    var articleLoaded = false

    var title: String = ""
    var description: String = ""
    var urlToImage: String = ""
    var source: String = ""
}
