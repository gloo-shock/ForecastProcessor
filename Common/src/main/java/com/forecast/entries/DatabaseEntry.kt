package com.forecast.entries

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class DatabaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0
}