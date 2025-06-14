package com.rafalskrzypczyk.ninetyvalues.utils

import android.content.Context
import com.rafalskrzypczyk.ninetyvalues.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.toFormattedDate(context: Context): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = this@toFormattedDate }

    val dateFormatSameYear = SimpleDateFormat("dd.MM", Locale.getDefault())
    val dateFormatFull = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val sameDay = now.get(Calendar.YEAR) == date.get(Calendar.YEAR)
            && now.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)

    val yesterday = now.get(Calendar.YEAR) == date.get(Calendar.YEAR)
            && now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR) == 1

    return when {
        sameDay -> context.getString(R.string.txt_today)
        yesterday -> context.getString(R.string.txt_yesterday)
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) -> dateFormatSameYear.format(date.time)
        else -> dateFormatFull.format(date.time)
    }
}