package com.rafalskrzypczyk.ninetyvalues.room

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity
import javax.inject.Provider

class PrepopulateRoomCallback @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dbProvider: Provider<NinetyValuesDB>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prepopulateWithNinetyValues(context = context)
        }
    }

    suspend fun prepopulateWithNinetyValues(context: Context) {
        try {
            val valueDao = dbProvider.get().valueDao

            val values: JSONArray =
                context.resources.openRawResource(R.raw.ninety_values).bufferedReader().use {
                    JSONArray(it.readText())
                }

            values.takeIf { it.length() > 0 }?.let { list ->
                val entitiesList = mutableListOf<ValueEntity>()

                for (index in 0 until list.length()) {
                    val valueObj = list.getJSONObject(index)
                    entitiesList.add(
                        ValueEntity(
                            pl = valueObj.getString("pl"),
                            en = valueObj.getString("en")
                        )
                    )
                }
                valueDao.populateWithValues(
                    entitiesList
                )
                Log.e("NinetyValues", "successfully pre-populated values into database")
            }
        } catch (exception: Exception) {
            Log.e("NinetyValues", exception.localizedMessage ?: "failed to pre-populate values into database")
        }
    }
}