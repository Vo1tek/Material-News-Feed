package com.voltek.newsfeed.domain.use_case

/**
 * Common interactor input parameters class.
 */
data class Parameter<out Type : Any?>(
        val flag: String = "",
        val item: Type? = null
)
