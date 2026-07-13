package com.clap2esp.app

object Logger {

    private val logs = mutableListOf<String>()

    fun log(message: String) {

        val time = java.text.SimpleDateFormat(
            "HH:mm:ss",
            java.util.Locale.getDefault()
        ).format(java.util.Date())

        logs.add("[$time] $message")

        if (logs.size > 300) {
            logs.removeAt(0)
        }
    }

    fun getLogs(): String {
        return logs.joinToString("\n")
    }

    fun clear() {
        logs.clear()
    }
}
