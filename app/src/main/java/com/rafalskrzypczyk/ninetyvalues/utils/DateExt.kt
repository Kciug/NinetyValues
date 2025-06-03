package com.rafalskrzypczyk.ninetyvalues.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.rafalskrzypczyk.ninetyvalues.R

fun Long.toFormattedDate(context: Context): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = this@toFormattedDate }

    val dateFormatSameYear = SimpleDateFormat("dd.MM", Locale.getDefault())
    val dateFormatFull = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val daysDiff = TimeUnit.MILLISECONDS.toDays(now.timeInMillis - date.timeInMillis).toInt()

    return when {
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) && daysDiff == 0 -> context.getString(R.string.txt_today)
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) && daysDiff == 1 -> context.getString(R.string.txt_yesterday)
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) -> dateFormatSameYear.format(Date(this))
        else -> dateFormatFull.format(Date(this))
    }
}