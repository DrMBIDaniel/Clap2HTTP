package com.clap2esp.app

import java.net.HttpURLConnection
import java.net.URL

class HttpWorker(
    private val serverIp: String
) : Thread() {

    @Volatile
    private var running = true

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

            Logger.log(
                "HTTP $command -> $code"
            )

            connection.disconnect()

        } catch (e: Exception) {

            Logger.log(
                "HTTP FAILED $command ${e.message}"
            )

            // Пока просто возвращаем команду обратно
            // в очередь.
            HttpQueue.push(command)

            sleep(500)
        }
    }
}
