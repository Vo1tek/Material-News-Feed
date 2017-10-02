package com.voltek.newsfeed.domain

import com.voltek.newsfeed.data.network.entity.ArticleAPI
import com.voltek.newsfeed.data.storage.entity.SourceDB
import com.voltek.newsfeed.data.network.entity.SourceAPI
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.entity.SourceUI

object Mapper {

    fun articleAPItoUI(item: ArticleAPI): ArticleUI =
            ArticleUI(
                    title = item.title,
                    description = item.description,
                    url = item.url,
                    urlToImage = item.urlToImage
            )

    fun sourceAPItoDB(item: SourceAPI): SourceDB {
        val source = SourceDB()
        with(source) {
            id = item.id ?: ""
            name = item.name ?: ""
            description = item.description ?: ""
            url = item.url ?: ""
            category = item.category ?: ""
            country = item.country ?: ""
            isEnabled = item.isEnabled ?: false
        }
        return source
    }

    fun sourceDBtoUI(item: SourceDB): SourceUI =
            SourceUI(
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    url = item.url,
                    category = item.category,
                    country = item.country,
                    isEnabled = item.isEnabled
            )

    fun sourceAPItoUI(item: SourceAPI): SourceUI =
            SourceUI(
                    id = item.id ?: "",
                    name = item.name ?: "",
                    description = item.description ?: "",
                    url = item.url ?: "",
                    category = item.category ?: "",
                    country = item.country ?: "",
                    isEnabled = item.isEnabled ?: false
            )
}
