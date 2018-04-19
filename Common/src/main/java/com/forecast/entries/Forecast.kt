package com.forecast.entries


import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity

@Entity
class Forecast(@JsonProperty hostScore: Int, @JsonProperty guestScore: Int)
    : AbstractResult(hostScore, guestScore, false) {
    constructor() : this(-1, -1)
}