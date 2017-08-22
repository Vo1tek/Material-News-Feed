package com.voltek.newsfeed.data.storage

import com.vicpin.krealmextensions.*
import com.voltek.newsfeed.data.entity.SourceRAW

class NewsSourcesStorage {

    fun queryAll(): List<SourceRAW> = SourceRAW().queryAll()

    fun queryEnabled(): List<SourceRAW> =
            SourceRAW().query { query -> query.equalTo("isEnabled", true) }

    fun queryCategory(category: String): List<SourceRAW> =
            SourceRAW().query { query -> query.equalTo("category", category.toLowerCase()) }

    fun findById(id: String): SourceRAW? =
            SourceRAW().queryFirst { query -> query.equalTo("id", id) }

    fun save(items: List<SourceRAW>) {
        items.saveAll()
    }

    fun deleteAll() {
        SourceRAW().deleteAll()
    }
}
