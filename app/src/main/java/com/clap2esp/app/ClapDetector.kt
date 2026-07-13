package com.clap2esp.app

enum class ClapType {
    NONE,
    SINGLE_CLAP,
    DOUBLE_CLAP
}

class ClapDetector {

    private val analyzer = SignalAnalyzer()

    private var waitingSecond = false

    private var firstClapTime = 0L

    private val doubleWindow = 800L

    private val minGap = 120L

    private var lastAccepted = 0L

    fun detect(buffer: ShortArray): ClapType {

        val signal = analyzer.analyze(buffer)

        var score = 0

        // 1. Громкость
        if (signal.peak > 10000)
            score += 30

        // 2. Средняя энергия
        if (signal.rms < signal.peak * 0.45)
            score += 20

        // 3. Высокое количество переходов через ноль
        if (signal.zeroCrossings > 140)
            score += 20

        // 4. Быстрый фронт
        if (signal.attack < 30)
            score += 15

        // 5. Быстрое затухание
        if (signal.decay < 40)
            score += 15

        if (score < 70)
            return ClapType.NONE

        Logger.log(
            "Peak=${signal.peak}  RMS=${signal.rms.toInt()}  Score=$score"
        )

        val now = System.currentTimeMillis()

        if (now - lastAccepted < minGap)
            return ClapType.NONE

        lastAccepted = now

        if (!waitingSecond) {

            waitingSecond = true

            firstClapTime = now

            Logger.log("Possible clap")

            return ClapType.NONE
        }

        val delay = now - firstClapTime

        if (delay <= doubleWindow) {

            waitingSecond = false

            Logger.log("DOUBLE delay=${delay}ms")

            return ClapType.DOUBLE_CLAP
        }

        firstClapTime = now

        return ClapType.NONE
    }

    fun checkSingleClapTimeout(): ClapType {

        if (
            waitingSecond &&
            System.currentTimeMillis() - firstClapTime > doubleWindow
        ) {

            waitingSecond = false

            Logger.log("SINGLE")

            return ClapType.SINGLE_CLAP
        }

        return ClapType.NONE
    }

}
