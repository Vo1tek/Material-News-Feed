package com.voltek.newsfeed.domain

import com.voltek.newsfeed.data.entity.ArticleRAW
import com.voltek.newsfeed.data.entity.SourceDB
import com.voltek.newsfeed.data.entity.SourceRAW
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.entity.SourceUI

object Mapper {

    fun articleRAWtoUI(item: ArticleRAW): ArticleUI =
            ArticleUI(
                    title = item.title,
                    description = item.description,
                    url = item.url,
                    urlToImage = item.urlToImage
            )

    fun sourceRAWtoUI(item: SourceRAW): SourceUI =
            SourceUI(
                    id = item.id ?: "",
                    name = item.name ?: "",
                    description = item.description ?: "",
                    url = item.url ?: "",
                    category = item.category ?: "",
                    country = item.country ?: "",
                    isEnabled = item.isEnabled ?: false
            )

    fun sourceRAWtoDB(item: SourceRAW): SourceDB {
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
}
