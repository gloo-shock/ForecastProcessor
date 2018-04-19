package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "person_forecast_link")
data class PersonForecast(@JsonProperty @ManyToOne(cascade = [CascadeType.MERGE]) val person: Person,
                          @JsonProperty @OneToMany(cascade = [CascadeType.ALL]) val forecasts: List<ForecastResult>) : DatabaseEntry() {
    constructor() : this(Person(), ArrayList())
}