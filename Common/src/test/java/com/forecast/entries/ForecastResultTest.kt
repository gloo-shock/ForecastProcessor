package com.forecast.entries

import com.forecast.entries.ForecastResult.Companion.parseFromString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ForecastResultTest {
    @Test
    internal fun computeScore() {
        val match = Match(Team("a"), Team("b"))
        assertThat(ForecastResult(match, Forecast(1, 2), Forecast(1, 2)).computeScore()).isEqualTo(4);
        assertThat(ForecastResult(match, Forecast(1, 1), Forecast(0, 0)).computeScore()).isEqualTo(3);
        assertThat(ForecastResult(match, Forecast(2, 1), Forecast(1, 2)).computeScore()).isEqualTo(0);
        assertThat(ForecastResult(match, Forecast(2, 1), Forecast(2, 1)).computeScore()).isEqualTo(4);
        assertThat(ForecastResult(match, Forecast(2, 1), Forecast(3, 1)).computeScore()).isEqualTo(2);
        assertThat(ForecastResult(match, Forecast(2, 1), Forecast(3, 2)).computeScore()).isEqualTo(3);
        assertThat(ForecastResult(match, Forecast(1, 2), Forecast(2, 3)).computeScore()).isEqualTo(3);
    }

    @Test
    internal fun parse() {
        val result = ForecastResult(Match(Team("РБ Лейпциг"), Team("Шальке 04")), Forecast(1, 2), null)

        assertThat(parseFromString("РБ Лейпциг - Шальке 04 1-2")).isEqualTo(result)
        assertThat(parseFromString("РБ Лейпциг 1-2 Шальке 04 ")).isEqualTo(result)
        assertThat(parseFromString("РБ Лейпциг-Шальке 04 1-2")).isEqualTo(result)
    }
}