package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class PersonForecast(@JsonProperty @OneToOne val person: Person,
                          @JsonProperty @OneToMany val forecasts: List<ForecastResult>) : DatabaseEntry()