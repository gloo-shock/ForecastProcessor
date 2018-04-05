package com.forecast.entries

import javax.persistence.Entity

@Entity
data class Person(val firstName: String,
                  val lastName: String) : DatabaseEntry() {

    constructor() : this("", "")

    override fun toString(): String {
        return "$firstName $lastName"
    }
}

