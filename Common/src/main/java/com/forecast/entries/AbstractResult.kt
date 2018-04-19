package com.forecast.entries

import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.Table

@Entity
@Inheritance
@Table(name = "forecasts_and_results")
abstract class AbstractResult(val hostScore: Int,
                              val guestScore: Int,
                              val isMatchResult: Boolean) : DatabaseEntry() {
    fun getDiff(): Int {
        return hostScore - guestScore;
    }
}