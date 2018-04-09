package com.forecast.entries

import java.lang.System.currentTimeMillis
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
data class Tour(@OneToMany val forecasts: List<PersonForecast>, @OneToMany val results: List<Result>) : DatabaseEntry() {
    val timestamp: Long = currentTimeMillis();
}