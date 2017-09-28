package com.voltek.newsfeed.presentation.entity

import org.parceler.Parcel

@Parcel
data class ArticleUI(
        val title: String? = null,
        val description: String? = null,
        val url: String? = null,
        val urlToImage: String? = null
) {
    fun isEmpty() = (title == null && description == null && url == null && urlToImage == null)

    var source: String = ""
}
