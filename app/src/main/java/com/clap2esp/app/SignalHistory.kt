package com.clap2esp.app


class SignalHistory(
    private val maxSize: Int = 12
) {


    private val history =
        ArrayDeque<SignalFeatures>()



    fun add(
        signal: SignalFeatures
    ) {


        if (history.size >= maxSize) {

            history.removeFirst()

        }


        history.addLast(signal)

    }





    fun getAll(): List<SignalFeatures> {

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
