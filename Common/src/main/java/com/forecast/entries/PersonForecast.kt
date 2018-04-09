package com.forecast.entries

import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class PersonForecast(@OneToOne val person: Person, @OneToMany val forecasts: List<Forecast>) : DatabaseEntry()