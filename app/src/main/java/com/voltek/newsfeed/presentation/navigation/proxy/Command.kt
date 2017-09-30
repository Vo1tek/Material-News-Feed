package com.voltek.newsfeed.presentation.navigation.proxy

/**
 * Basic class for navigation commands. Extend from it and add any properties, that you need.
 *
 * @id command unique identifier, assigned only if command get queued.
 * @addToQueue should command be added to queue, if it cannot be executed instantly.
 */
abstract class Command {

    var id: Int = 0
    var addToQueue: Boolean = true
}
