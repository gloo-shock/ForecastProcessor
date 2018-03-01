package com.forecast.entries

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ForecastResultTest {
    @Test
    internal fun computeScore() {
        val match = Match(Team("a"), Team("b"))
        assertThat(ForecastResult(Forecast(match, 1, 1), Forecast(match, 1, 1)).computeScore()).isEqualTo(4);
        assertThat(ForecastResult(Forecast(match, 1, 1), Forecast(match, 0, 0)).computeScore()).isEqualTo(3);
        assertThat(ForecastResult(Forecast(match, 1, 1), Forecast(match, 1, 2)).computeScore()).isEqualTo(0);
        assertThat(ForecastResult(Forecast(match, 2, 1), Forecast(match, 1, 2)).computeScore()).isEqualTo(0);
        assertThat(ForecastResult(Forecast(match, 2, 1), Forecast(match, 2, 1)).computeScore()).isEqualTo(4);
        assertThat(ForecastResult(Forecast(match, 2, 1), Forecast(match, 3, 1)).computeScore()).isEqualTo(2);
        assertThat(ForecastResult(Forecast(match, 2, 1), Forecast(match, 3, 2)).computeScore()).isEqualTo(3);
        assertThat(ForecastResult(Forecast(match, 1, 2), Forecast(match, 2, 3)).computeScore()).isEqualTo(3);
    }
}