package com.forecast.entries

data class Match(val host: Team,
                 val guest: Team) {
    override fun toString(): String {
        return host.name + " - " + guest.name
    }
}