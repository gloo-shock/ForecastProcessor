package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.NumberFormatException
import java.util.Arrays.asList
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.persistence.*

const val NOTHING = 0
const val RIGHT_RESULT = 2
const val RIGHT_DIFF = 3
const val RIGHT_SCORE = 4

const val BIG_DASH = 8212.toChar()

@Entity
@Table(name = "forecast_results")
class ForecastResult(@JsonProperty @ManyToOne(cascade = [CascadeType.MERGE]) val match: Match,
                     @JsonProperty @OneToOne(cascade = [CascadeType.ALL]) val forecast: Forecast,
                     @JsonProperty @ManyToOne(cascade = [CascadeType.MERGE]) var result: Result?) : DatabaseEntry() {

    constructor() : this(Match(), Forecast(), Result())

    var score = 0

    fun updateScore() {
        score = computeScore();
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

    fun setResultAndUpdateScore(result: Result?) {
        this.result = result
        updateScore()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ForecastResult

        if (match != other.match) return false
        if (forecast != other.forecast) return false
        if (result != other.result) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = match.hashCode()
        result1 = 31 * result1 + forecast.hashCode()
        result1 = 31 * result1 + (result?.hashCode() ?: 0)
        return result1
    }

    override fun toString(): String {
        return "ForecastResult(match=$match, forecast=$forecast, result=$result, score=$score)"
    }


    init {
        updateScore();
    }


    companion object {
        @JvmStatic
        fun parseFromString(forecastString: String): ForecastResult? {
            var modifiedString = forecastString.replace(":", "-").replace(BIG_DASH, '-')
            modifiedString = modifiedString.replace(Pattern.compile(" *- *").toRegex(), "-")
            var parts = modifiedString.split("-").stream().map { it -> it.trim() }.collect(Collectors.toList())
            if (parts.size == 2) {
                parts = extractAllPartsMiddleScore(parts)
            } else {
                parts = parts.stream().flatMap { extractAllParts(it, parts.indexOf(it) == 1) }
                        .collect(Collectors.toList())
            }
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

        private fun extractAllPartsMiddleScore(parts: List<String>): List<String> {
            if (parts.size != 2) {
                throw IllegalArgumentException("The score must be in the middle")
            }
            val firstTeamSpace = parts[0].lastIndexOf(" ")
            val secondTeamSpace = parts[1].indexOf(" ")
            if (firstTeamSpace < 0 || secondTeamSpace < 0) {
                throw IllegalArgumentException("The score must be in the middle")
            }
            return asList(parts[0].substring(0, firstTeamSpace),
                    parts[1].substring(secondTeamSpace + 1),
                    parts[0].substring(firstTeamSpace + 1),
                    parts[1].substring(0, secondTeamSpace))
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