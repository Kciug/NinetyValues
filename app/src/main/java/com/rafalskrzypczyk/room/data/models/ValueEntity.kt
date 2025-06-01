package com.rafalskrzypczyk.room.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafalskrzypczyk.room.RoomConstants

@Entity(tableName = RoomConstants.VALUES_TABLE)
data class ValueEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)