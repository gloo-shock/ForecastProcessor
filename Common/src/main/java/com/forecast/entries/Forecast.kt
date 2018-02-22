package com.forecast.entries

data class Forecast(val host: Team,
                    val guest: Team,
                    val hostScore: Int,
                    val guestScore: Int)