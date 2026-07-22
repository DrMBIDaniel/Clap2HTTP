package com.clap2esp.app

import java.util.concurrent.ConcurrentLinkedQueue

object HttpQueue {

    private val queue =
        ConcurrentLinkedQueue<String>()

    fun push(command: String) {

        queue.offer(command)

        Logger.log(
            "HTTP queued: $command"
        )
    }

    fun pop(): String? {

        return queue.poll()
    }

    fun isEmpty(): Boolean {

        return queue.isEmpty()
    }

    fun size(): Int {

        return queue.size
    }
}
