package com.clap2esp.app

import java.net.HttpURLConnection
import java.net.URL

class HttpWorker(
    private val serverIp: String
) : Thread() {

    @Volatile
    private var running = true

    companion object {

        private const val MAX_RETRIES = 3

        private const val RETRY_DELAY = 500L
    }

    override fun run() {

        while (running) {

            val command = HttpQueue.pop()

            if (command == null) {

                sleep(30)
                continue
            }

            send(command)
        }
    }

    fun shutdown() {
        running = false
    }

    private fun send(command: String) {

        var attempt = 1

        while (attempt <= MAX_RETRIES) {

            try {

                val url =
                    URL("http://$serverIp/$command")

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                connection.connectTimeout = 1500
                connection.readTimeout = 1500

                val code =
                    connection.responseCode

                connection.disconnect()

                Logger.log(
                    "HTTP $command success ($code)"
                )

                return

            } catch (e: Exception) {

                Logger.log(
                    "HTTP retry $attempt/$MAX_RETRIES : ${e.message}"
                )

                attempt++

                sleep(RETRY_DELAY)
            }
        }

        Logger.log(
            "HTTP FAILED after $MAX_RETRIES attempts"
        )
    }
}
