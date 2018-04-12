package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class Match(@JsonProperty @OneToOne val host: Team,
                 @JsonProperty @OneToOne val guest: Team) : DatabaseEntry() {

    constructor() : this(Team(""), Team(""))

    override fun toString(): String {
        return host.name + " - " + guest.name
    }
}