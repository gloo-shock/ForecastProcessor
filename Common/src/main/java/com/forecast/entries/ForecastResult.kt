package com.forecast.entries

const val NOTHING = 0
const val RIGHT_RESULT = 2
const val RIGHT_DIFF = 3
const val RIGHT_SCORE = 4

class ForecastResult {
    val forecast: Forecast
    val result: Forecast?
    val score: Int

    constructor(forecast: Forecast, result: Forecast?) {
        this.forecast = forecast
        this.result = result
        this.score = computeScore()
    }

    public fun computeScore(): Int {
        if (result == null) {
            return NOTHING
        }
        val forecastDiff = forecast.getDiff().toDouble()
        val resultDiff = result.getDiff().toDouble()
        if (Math.signum(forecastDiff) != Math.signum(resultDiff)) {
            return NOTHING;
        }
        if (forecastDiff == resultDiff) {
            if (forecast.hostScore == result.hostScore) return RIGHT_SCORE else return RIGHT_DIFF;
        }
        return RIGHT_RESULT
    }
}