package com.voltek.newsfeed.presentation.navigation.proxy

/**
 * Entry point for navigation commands.
 */
interface Router {

    fun execute(command: Command)
}
