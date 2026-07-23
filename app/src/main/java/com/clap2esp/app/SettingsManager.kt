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

    fun saveTrainingData(
    averagePeak: Double,
    averageRms: Double,
    averageHighRatio: Double,
    averageFlux: Double,
    averageRollOff: Double,
    minPeak: Int,
    maxPeak: Int
) {

    prefs.edit()

        .putBoolean("trained", true)

        .putFloat("avg_peak", averagePeak.toFloat())
        .putFloat("avg_rms", averageRms.toFloat())
        .putFloat("avg_ratio", averageHighRatio.toFloat())
        .putFloat("avg_flux", averageFlux.toFloat())
        .putFloat("avg_rolloff", averageRollOff.toFloat())

        .putInt("min_peak", minPeak)
        .putInt("max_peak", maxPeak)

        .apply()
}

fun isTrained(): Boolean =
    prefs.getBoolean("trained", false)

fun averagePeak(): Double =
    prefs.getFloat("avg_peak", 0f).toDouble()

fun averageRms(): Double =
    prefs.getFloat("avg_rms", 0f).toDouble()

fun averageHighRatio(): Double =
    prefs.getFloat("avg_ratio", 0f).toDouble()

fun averageFlux(): Double =
    prefs.getFloat("avg_flux", 0f).toDouble()

fun averageRollOff(): Double =
    prefs.getFloat("avg_rolloff", 0f).toDouble()

fun minPeak(): Int =
    prefs.getInt("min_peak", 0)

fun maxPeak(): Int =
    prefs.getInt("max_peak", 0)

fun clearTraining() {

    prefs.edit()

        .remove("trained")
        .remove("avg_peak")
        .remove("avg_rms")
        .remove("avg_ratio")
        .remove("avg_flux")
        .remove("avg_rolloff")
        .remove("min_peak")
        .remove("max_peak")

        .apply()
}
}
