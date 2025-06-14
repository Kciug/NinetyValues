package com.rafalskrzypczyk.ninetyvalues.presentation.entries_history

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(state.entries) {
        if(state.entries.isNotEmpty()) visible = true
    }

    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(R.string.title_entries_history),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 100))
        ){
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxSize()
            ) {
                items(state.entries) { item ->
                    EntryCard(item) { id -> onNavigateToEntry(id) }
                }
            }
        }
    }
}

@Composable
fun EntryCard(
    entry: Entry,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick(entry.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = stringResource(R.string.ic_desc_date),
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = entry.timestamp ?: stringResource(R.string.missing_date),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.open_entry),
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = stringResource(R.string.open_entry),
                )
            }
        }
    }
}

@Composable
@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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