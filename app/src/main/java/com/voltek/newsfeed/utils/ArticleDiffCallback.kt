package com.voltek.newsfeed.utils

import android.support.v7.util.DiffUtil
import com.voltek.newsfeed.presentation.entity.ArticleUI

class ArticleDiffCallback(
        private val oldList: List<ArticleUI>, private val newList: List<ArticleUI>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = oldList[oldItemPosition].url == newList[newItemPosition].url

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = oldList[oldItemPosition] == newList[newItemPosition]
}
