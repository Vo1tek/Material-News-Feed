package com.voltek.newsfeed.navigation.command

import com.voltek.newsfeed.data.entity.ArticleRAW
import com.voltek.newsfeed.domain.entity.ArticleUI
import com.voltek.newsfeed.navigation.proxy.Command

class CommandOpenArticleDetailsScreen(val article: ArticleUI) : Command()
