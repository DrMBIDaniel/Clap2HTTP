package com.clap2esp.app

data class TrainingSample(

    val peak: Int,

    val rms: Double,

    val highRatio: Double,

    val zeroCrossings: Int,

    val impulseWidth: Int,

    val spectralCentroid: Double,

    val spectralFlatness: Double,

    val spectralFlux: Double,

    val spectralRollOff: Double

)
