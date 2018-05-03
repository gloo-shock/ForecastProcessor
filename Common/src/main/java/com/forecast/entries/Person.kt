package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "persons")
data class Person(@JsonProperty val firstName: String,
                  @JsonProperty val lastName: String,
                  @JsonProperty val forecastTeam: String?) : DatabaseEntry() {

    constructor() : this("", "", null)
    constructor(firstName: String, lastName: String) : this(firstName, lastName, null)

    override fun toString(): String {
        return "$firstName $lastName"
    }
}

