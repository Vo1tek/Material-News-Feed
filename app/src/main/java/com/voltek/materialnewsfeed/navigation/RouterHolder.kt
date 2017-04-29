package com.voltek.materialnewsfeed.navigation

import com.voltek.materialnewsfeed.navigation.command.CommandNavigatorAttached
import com.voltek.materialnewsfeed.navigation.proxy.*
import io.reactivex.subjects.PublishSubject

/**
 * Manages app navigation, lifecycle tied to app process. Can hold only one navigator at a time.
 * When Router receives command, it will be executed by current navigator or added to queue,
 * if there is no navigator attached or current navigator can not execute it.
 * When new navigator get attached, holder will try to execute queued commands.
 */
class RouterHolder : Router, NavigatorBinder {

    val CommandsFeed: PublishSubject<Command> = PublishSubject.create()

    private var navigator: Navigator? = null

    private var commandsQueue: ArrayList<Command> = ArrayList()

    init {
        CommandsFeed
                .filter { !runCommand(it) } // If command executed
                .filter { !runQueue(it) } // If new navigator attached
                .subscribe({ addToQueue(it) }) // Else, try to add command to queue
    }

    // Router
    override fun execute(command: Command) {
        CommandsFeed.onNext(command)
    }

    // NavigatorBinder
    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
        CommandsFeed.onNext(CommandNavigatorAttached())
    }

    override fun removeNavigator() {
        this.navigator = null
    }

    // Private logic

    /**
     * @return If navigator = null returns false, if command executed - returns true,
     *         if command cannot be executed - false.
     */
    private fun runCommand(command: Command): Boolean = navigator?.executeCommand(command) ?: false

    /**
     * @return true, if new navigator has been attached (tries to execute all commands in queue)
     *         else returns false. (Without queue execution)
     */
    private fun runQueue(command: Command): Boolean {
        if (command is CommandNavigatorAttached) {
            commandsQueue
                    .filter { runCommand(it) }
                    .forEach { removeFromQueue(it) }

            return true
        } else {
            return false
        }
    }

    /**
     * @return true, if command was added to queue, else false.
     */
    private fun addToQueue(command: Command): Boolean {
        if (command.addToQueue) {
            if (!commandsQueue.isEmpty())
                command.id = commandsQueue[commandsQueue.lastIndex].id + 1
            return commandsQueue.add(command)
        } else {
            return false
        }
    }

    /**
     * @return true, if command removed successfully, else false
     */
    private fun removeFromQueue(command: Command) = commandsQueue.remove(command)
}