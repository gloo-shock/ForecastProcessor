package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "teams", uniqueConstraints = [(UniqueConstraint(columnNames = ["name", "league"]))])
data class Team(@JsonProperty val name: String, val name2: String?, val name3: String?, val league: String) : DatabaseEntry() {
    constructor() : this("")
    constructor(name: String) : this(name, null, null, League.UNKNOWN.leagueString)

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