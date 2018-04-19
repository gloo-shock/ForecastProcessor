package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "teams")
data class Team(@JsonProperty @Column(unique = true) val name: String) : DatabaseEntry() {
    constructor() : this("")

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Team) {
            return false
        }
        return name.equals(other.name, true)
    }

    override fun hashCode(): Int {
        return name.toLowerCase().hashCode()
    }
}