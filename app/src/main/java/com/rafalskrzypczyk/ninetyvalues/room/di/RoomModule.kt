package com.rafalskrzypczyk.ninetyvalues.room.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafalskrzypczyk.ninetyvalues.room.NinetyValuesDB
import com.rafalskrzypczyk.ninetyvalues.room.PrepopulateRoomCallback
import com.rafalskrzypczyk.ninetyvalues.room.data.dao.EntryDao
import com.rafalskrzypczyk.ninetyvalues.room.data.dao.ValueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomCallback(
        @ApplicationContext context: Context,
        dbProvider: Provider<NinetyValuesDB>
    ): RoomDatabase.Callback {
        return PrepopulateRoomCallback(context, dbProvider)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
        callback: RoomDatabase.Callback
    ): NinetyValuesDB {
        return Room.databaseBuilder(context, NinetyValuesDB::class.java, "ninety_values_db")
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideValueDao(database: NinetyValuesDB): ValueDao {
        return database.valueDao
    }

    @Provides
    fun provideEntryDao(database: NinetyValuesDB): EntryDao {
        return database.entryDao
    }
}