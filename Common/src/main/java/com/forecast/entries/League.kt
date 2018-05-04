package com.forecast.entries

enum class League(val leagueString: String) {
    RUSSIA("Россия"),
    SPAIN("Испания"),
    ENGLAND("Англия"),
    ITALY("Италия"),
    FRANCE("Франция"),
    GERMANY("Германия"),
    UNKNOWN("Остальной мир");

    fun getLeagueById(id: String): League {
        return values().findLast { it.leagueString == id } ?: UNKNOWN
    }
}