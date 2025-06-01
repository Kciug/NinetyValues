package com.rafalskrzypczyk.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafalskrzypczyk.room.RoomConstants
import com.rafalskrzypczyk.room.data.models.ValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ValueDao {
    @Query("SELECT * FROM ${RoomConstants.VALUES_TABLE}")
    fun getAllValues(): Flow<List<ValueEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun populateWithValues(valueEntities: List<ValueEntity>)
}