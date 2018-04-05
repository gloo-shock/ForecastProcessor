package com.forecast.entries

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

abstract class DatabaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = 0
}