package com.forecast.entries

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Persistent
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "matches")
data class Match(@JsonProperty @Persistent @ManyToOne(cascade = [CascadeType.MERGE]) val host: Team,
                 @JsonProperty @Persistent @ManyToOne(cascade = [CascadeType.MERGE]) val guest: Team) : DatabaseEntry() {

    constructor() : this(Team(""), Team(""))

    override fun toString(): String {
        return host.name + " - " + guest.name
    }
}