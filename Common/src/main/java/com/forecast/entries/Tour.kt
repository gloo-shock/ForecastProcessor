package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.System.currentTimeMillis
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
data class Tour(@JsonProperty @OneToMany val forecasts: List<PersonForecast>,
                @JsonProperty @OneToMany val results: List<Result>) : DatabaseEntry() {
    val timestamp: Long = currentTimeMillis();
}