package com.rafalskrzypczyk.ninetyvalues.presentation.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.ui.composables.NavigationTopBar
import com.rafalskrzypczyk.ninetyvalues.ui.theme.NinetyValuesTheme

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onNavigateToLastEntry: (Long) -> Unit,
    onNavigateToEntriesList: () -> Unit,
    onNavigateToNewEntry: () -> Unit
) {
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(R.string.app_name)
            ) {
                IconButton(onClick = onNavigateToEntriesList) {
                    Icon(
                        imageVector = Icons.Default.HistoryEdu,
                        contentDescription = stringResource(R.string.ic_desc_open_entries_history),
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(state.lastEntryDate.isNullOrEmpty().not()) {
                EntriesSection(
                    lastEntryDate = state.lastEntryDate,
                    onNavigateToLastEntry = { state.lastEntryId?.let { onNavigateToLastEntry(it) } }
                )
            }
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.app_name)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onNavigateToNewEntry) {
                    Text(stringResource(R.string.btn_start))
                }
            }
        }
    }
}

@Composable
fun EntriesSection(
    lastEntryDate: String,
    onNavigateToLastEntry: () -> Unit
) {
    Row (
        modifier = Modifier
            .clickable(onClick = onNavigateToLastEntry)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.last_entry_title))
        Text(
            text = lastEntryDate,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.ic_desc_open_last_entry)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeScreen() {
    NinetyValuesTheme {
        Surface {
            HomeScreen(
                state = HomeScreenState(
                    lastEntryDate = "20.04.1889"
                ),
                onNavigateToLastEntry = {},
                onNavigateToEntriesList = {},
                onNavigateToNewEntry = {},
            )
        }
    }
}