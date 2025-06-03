package com.rafalskrzypczyk.ninetyvalues.presentation.entries_history

import com.rafalskrzypczyk.ninetyvalues.domain.models.Entry

data class EntriesHistoryState(
    val entries: List<Entry> = emptyList()
)