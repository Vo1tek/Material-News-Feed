package com.voltek.newsfeed.presentation.base

/**
 * Base ViewModel class.
 *
 * @param subscriber function, that will be called when model updates
 */
abstract class BaseModel(private val subscriber: (BaseModel) -> Unit) {

    /**
     * Call this method to render new model state
     */
    fun update() {
        subscriber.invoke(this@BaseModel)
    }
}
