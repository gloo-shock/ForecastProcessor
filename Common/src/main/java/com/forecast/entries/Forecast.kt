package com.forecast.entries

import javax.persistence.Entity

@Entity
class Forecast(hostScore: Int, guestScore: Int)
    : AbstractResult(hostScore, guestScore, false)