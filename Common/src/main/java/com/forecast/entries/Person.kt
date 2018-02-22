package com.forecast.entries

data class Person(val firstName: String,
                  val lastName: String) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}

