package com.clap2esp.app

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Logger {

    private val logs = mutableListOf<String>()

    private var listener: (() -> Unit)? = null

    fun setOnLogChanged(callback: () -> Unit) {
        listener = callback
    }

    fun log(message: String) {

        val time = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        logs.add("[$time] $message")

        if (logs.size > 300) {
            logs.removeAt(0)
        }

        listener?.invoke()
    }

    fun getLogs(): String {

        return logs.joinToString("\n")

    }

    fun clear() {

        logs.clear()

        listener?.invoke()

    }
}
