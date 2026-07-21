package com.clap2esp.app

class AdaptiveThreshold {

    private var threshold = 10.0

    fun currentThreshold(): Double {
        return threshold
    }

    fun update(success: Boolean) {

        if (success) {

            threshold -= 0.05

            if (threshold < 6.0) {
                threshold = 6.0
            }

        } else {

            threshold += 0.02

            if (threshold > 12.0) {
                threshold = 12.0
            }
        }
    }
}
