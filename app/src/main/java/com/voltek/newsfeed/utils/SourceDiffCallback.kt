package com.voltek.newsfeed.utils

import android.support.v7.util.DiffUtil
import com.voltek.newsfeed.presentation.entity.SourceUI

class SourceDiffCallback(
        private val oldList: List<SourceUI>, private val newList: List<SourceUI>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = oldList[oldItemPosition] == newList[newItemPosition]
}
