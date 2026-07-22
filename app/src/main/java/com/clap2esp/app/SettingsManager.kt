package com.clap2esp.app

import android.content.Context

class SettingsManager(
    context: Context
) {

    private val prefs =
        context.getSharedPreferences(
            "clap2http_settings",
            Context.MODE_PRIVATE
        )

    companion object {

        private const val KEY_IP =
            "server_ip"

        private const val DEFAULT_IP =
            "192.168.1.100"
    }

    fun getServerIp(): String {

        return prefs.getString(
            KEY_IP,
            DEFAULT_IP
        ) ?: DEFAULT_IP
    }

    fun setServerIp(ip: String) {

        prefs.edit()
            .putString(KEY_IP, ip)
            .apply()
    }
}
