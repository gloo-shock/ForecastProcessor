package com.forecast.entries

import java.lang.NumberFormatException
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

const val NOTHING = 0
const val RIGHT_RESULT = 2
const val RIGHT_DIFF = 3
const val RIGHT_SCORE = 4

class ForecastResult(val match: Match, val forecast: Forecast, var result: Forecast?) {
    var score = 0

    fun updateScore() {
        score = computeScore();
    }

    fun getScoreString(): String {
        val rest = score % 10
        if (rest == 1) {
            return score.toString() + " очко"
        }
        if (rest > 1 && rest < 5) {
            return score.toString() + " очка"
        }
        return score.toString() + " очков"
    }

    @Synchronized
    fun computeScore(): Int {
        if (result == null) {
            return NOTHING
        }
        val forecastDiff = forecast.getDiff().toDouble()
        val resultDiff = result!!.getDiff().toDouble()
        if (Math.signum(forecastDiff) != Math.signum(resultDiff)) {
            return NOTHING;
        }
        if (forecastDiff == resultDiff) {
            if (forecast.hostScore == result!!.hostScore) return RIGHT_SCORE else return RIGHT_DIFF;
        }
        return RIGHT_RESULT
    }

    fun setResultAndUpdateScore(result: Forecast?) {
        this.result = result
        updateScore()
    }

    init {
        updateScore();
    }

    companion object {
        @JvmStatic
        fun parseFromString(forecastString: String): ForecastResult? {
            var modifiedString = forecastString.replace(":", "-");
            modifiedString = modifiedString.replace(Pattern.compile(" *- *").toRegex(), "-")
            var parts = modifiedString.split("-")
            if (parts.size < 3) return null;

            parts = parts.stream().flatMap { extractAllParts(it, parts.indexOf(it) == 1) }
                    .collect(Collectors.toList())
            if (parts.size < 4) return null;
            try {
                return ForecastResult(Match(
                        Team(parts[0]),
                        Team(parts[1])),
                        Forecast(Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3])),
                        null
                )
            } catch (e: NumberFormatException) {
                return null
            }
        }

        @JvmStatic
        private fun extractAllParts(part: String, split: Boolean): Stream<String> {
            val trimmed = part.trim()
            if (split) {
                val space = trimmed.lastIndexOf(" ")
                if (space > 0) {
                    return Stream.of(trimmed.substring(0, space), trimmed.substring(space + 1))
                }
            }
            return Stream.of(trimmed)
        }
    }
}