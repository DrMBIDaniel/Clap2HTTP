package com.clap2esp.app

import kotlin.math.PI
import kotlin.math.cos

class HannWindow(
    private val size: Int
) {

    private val window = DoubleArray(size)

    init {
        for (i in 0 until size) {
            window[i] =
                0.5 * (
                    1.0 -
                    cos(
                        2.0 * PI * i / (size - 1)
                    )
                )
        }
    }

    fun apply(input: ShortArray): DoubleArray {

        val output = DoubleArray(size)

        val limit = minOf(input.size, size)

        for (i in 0 until limit) {
            output[i] =
                input[i].toDouble() * window[i]
        }
        return output
    }
}
