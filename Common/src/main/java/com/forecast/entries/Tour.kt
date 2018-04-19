package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.System.currentTimeMillis
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tours")
data class Tour(@JsonProperty @OneToMany(cascade = [CascadeType.ALL]) val personForecasts: MutableList<PersonForecast>) : DatabaseEntry() {

    constructor() : this(ArrayList())

    @JsonProperty
    val timestamp: Long = currentTimeMillis();

    fun getForecastResults(person: Person): List<ForecastResult> {
        return findByPerson(person)?.forecasts.orEmpty()
    }

    fun addPersonForecast(person: Person, forecastResult: List<ForecastResult>) {
        personForecasts.remove(findByPerson(person))
        personForecasts.add(PersonForecast(person, forecastResult))
    }

    private fun findByPerson(person: Person) = personForecasts.find { it.person == person }
}