package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity

@Entity
class Result(@JsonProperty hostScore: Int,
             @JsonProperty guestScore: Int)
    : AbstractResult(hostScore, guestScore, false)