package com.rafalskrzypczyk.ninetyvalues.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafalskrzypczyk.ninetyvalues.room.data.dao.EntryDao
import com.rafalskrzypczyk.ninetyvalues.room.data.dao.ValueDao
import com.rafalskrzypczyk.ninetyvalues.room.data.models.EntryEntity
import com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity
import com.rafalskrzypczyk.ninetyvalues.room.utils.Converters

@Database(
    entities = [
        ValueEntity::class,
        EntryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NinetyValuesDB : RoomDatabase() {
    abstract val valueDao: ValueDao
    abstract val entryDao: EntryDao
}