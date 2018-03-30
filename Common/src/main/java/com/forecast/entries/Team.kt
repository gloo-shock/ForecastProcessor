package com.forecast.entries

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.AUTO
import javax.persistence.Id

@Entity
data class Team(val name: String) {

    @Id
    @GeneratedValue(strategy = AUTO)
    val id: Long? = 0

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