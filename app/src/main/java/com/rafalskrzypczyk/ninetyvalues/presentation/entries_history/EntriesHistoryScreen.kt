package com.rafalskrzypczyk.ninetyvalues.presentation.entries_history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.domain.models.Entry
import com.rafalskrzypczyk.ninetyvalues.ui.composables.NavigationTopBar
import com.rafalskrzypczyk.ninetyvalues.ui.theme.NinetyValuesTheme

@Composable
fun EntriesHistoryScreen(
    state: EntriesHistoryState,
    onNavigateBack: () -> Unit,
    onNavigateToEntry: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(R.string.title_entries_history),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LazyColumn (modifier = modifier.fillMaxSize()) {
            items(state.entries) { item ->
                EntryCard(item) { id -> onNavigateToEntry(id) }
            }
        }
    }
}

@Composable
fun EntryCard(
    entry: Entry,
    onClick: (Long) -> Unit
) {
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clickable(onClick = { onClick(entry.id) })
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = stringResource(R.string.ic_desc_date),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = entry.timestamp ?: "",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview
private fun PreviewEntriesHistoryScreen() {
    NinetyValuesTheme {
        Surface {
            EntriesHistoryScreen(
                state = EntriesHistoryState(
                    entries = listOf(
                        Entry(
                            timestamp = "dzisiaj",
                            orderedValueIds = emptyList()
                        ),
                        Entry(
                            timestamp = "wczoraj",
                            orderedValueIds = emptyList()
                        ),
                        Entry(
                            timestamp = "02.05.2025",
                            orderedValueIds = emptyList()
                        ),
                        Entry(
                            timestamp = "06.09.1996",
                            orderedValueIds = emptyList()
                        )
                    )
                ),
                onNavigateBack = {}
            ) {}
        }
    }
}