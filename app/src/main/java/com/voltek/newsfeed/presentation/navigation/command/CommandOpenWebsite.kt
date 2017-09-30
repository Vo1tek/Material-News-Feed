package com.voltek.newsfeed.presentation.navigation.command

import com.voltek.newsfeed.presentation.navigation.proxy.Command

/**
 * Open url via implicit intent
 */
class CommandOpenWebsite(val url: String) : Command()
