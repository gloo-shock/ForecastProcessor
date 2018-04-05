package com.forecast.entries

import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class Match(@OneToOne val host: Team,
                 @OneToOne val guest: Team) : DatabaseEntry() {

    constructor() : this(Team(""), Team(""))

    override fun toString(): String {
        return host.name + " - " + guest.name
    }
}