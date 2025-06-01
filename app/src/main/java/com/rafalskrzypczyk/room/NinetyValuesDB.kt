package com.rafalskrzypczyk.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafalskrzypczyk.room.data.dao.ValueDao
import com.rafalskrzypczyk.room.data.models.ValueEntity

@Database(
    entities = [ValueEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NinetyValuesDB : RoomDatabase() {
    abstract val valueDao: ValueDao
}