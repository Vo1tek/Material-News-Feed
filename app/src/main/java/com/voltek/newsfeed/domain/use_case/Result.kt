package com.voltek.newsfeed.domain.use_case

/**
 * Generic class for interactor execution results.
 */
data class Result<out Type : Any?>(
        val data: Type? = null,
        val message: String = ""
)
