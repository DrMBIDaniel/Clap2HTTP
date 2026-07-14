package com.clap2esp.app

class SignalHistory(
    private val maxSize: Int = 10
) {

    private val history = ArrayDeque<SignalInfo>()

    fun add(signal: SignalInfo) {

        if (history.size >= maxSize) {
            history.removeFirst()
        }

        history.addLast(signal)
    }

    fun getAll(): List<SignalInfo> {
        return history.toList()
    }

    fun clear() {
        history.clear()
    }

    fun size(): Int {
        return history.size
    }

    fun isFull(): Boolean {
        return history.size >= maxSize
    }

}
