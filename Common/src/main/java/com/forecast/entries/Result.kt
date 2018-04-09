package com.forecast.entries

import javax.persistence.Entity

@Entity
class Result(hostScore: Int, guestScore: Int)
    : AbstractResult(hostScore, guestScore, false)