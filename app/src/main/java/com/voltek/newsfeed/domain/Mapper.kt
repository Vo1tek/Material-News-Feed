package com.voltek.newsfeed.domain

import com.voltek.newsfeed.data.entity.ArticleRAW
import com.voltek.newsfeed.data.entity.SourceRAW
import com.voltek.newsfeed.presentation.entity.ArticleUI
import com.voltek.newsfeed.presentation.entity.SourceUI

object Mapper {

    fun Article(item: ArticleRAW): ArticleUI =
            ArticleUI(
                    title = item.title,
                    description = item.description,
                    url = item.url,
                    urlToImage = item.urlToImage
            )

    fun Source(item: SourceRAW): SourceUI =
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
