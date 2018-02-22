package com.forecast.entries

data class Forecast(val match: Match,
                    val hostScore: Int,
                    val guestScore: Int)