package com.forecast.entries

data class Forecast(val hostScore: Int,
                    val guestScore: Int) {

    fun getDiff(): Int {
        return hostScore - guestScore;
    }
}