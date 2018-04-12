package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity

@Entity
data class Person(@JsonProperty val firstName: String,
                  @JsonProperty val lastName: String) : DatabaseEntry() {

    constructor() : this("", "")

    override fun toString(): String {
        return "$firstName $lastName"
    }
}

