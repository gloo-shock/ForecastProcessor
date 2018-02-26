package com.forecast.entries

import java.lang.Integer.parseInt
import java.lang.NumberFormatException
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

data class Forecast(val match: Match,
                    val hostScore: Int,
                    val guestScore: Int) {

    companion object {
        @JvmStatic
        fun parseFromString(forecastString: String): Forecast? {
            var modifiedString = forecastString.replace(":", "-");
            modifiedString = modifiedString.replace(Pattern.compile(" *- *").toRegex(), "-")
            var parts = modifiedString.split("-")
            if (parts.size < 3) return null;

            parts = parts.stream().flatMap { extractAllParts(it, parts.indexOf(it) == 1) }
                    .collect(Collectors.toList())
            if (parts.size < 4) return null;
            try {
                return Forecast(Match(
                        Team(parts[0]),
                        Team(parts[1])),
                        parseInt(parts[2]),
                        parseInt(parts[3])
                )
            } catch (e: NumberFormatException) {
                return null
            }
        }

        @JvmStatic
        private fun extractAllParts(part: String, split: Boolean): Stream<String> {
            return if (split) part.trim().split(" ").stream()
            else Stream.of(part.trim())
        }
    }
}