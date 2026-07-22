package com.clap2esp.app

class DecisionSmoother {

    private val history = ArrayDeque<Boolean>()

    private val historySize = 4

    fun update(candidate: Boolean): Boolean {

        history.addLast(candidate)

        while (history.size > historySize) {
            history.removeFirst()
        }

        var positives = 0

        for (value in history) {
            if (value) positives++
        }

        return positives >= 2
    }

    fun clear() {
        history.clear()
    }
}
