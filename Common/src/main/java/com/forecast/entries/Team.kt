package com.forecast.entries

data class Team(val name: String) {
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