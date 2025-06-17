package com.rafalskrzypczyk.ninetyvalues.room.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "value_items")
data class ValueEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pl: String,
    val en: String
)