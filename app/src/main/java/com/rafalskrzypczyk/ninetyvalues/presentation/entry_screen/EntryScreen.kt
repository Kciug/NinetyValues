package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.ui.composables.NavigationTopBar
import com.rafalskrzypczyk.ninetyvalues.ui.theme.NinetyValuesTheme

@Composable
fun EntryScreen(
    state: EntryState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(R.string.title_entry),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LazyColumn (modifier = modifier.fillMaxSize()) {
            item {
                Text(
                    text = buildAnnotatedString {
                        append("${stringResource(R.string.entry_header)} ${state.headerMessageDateJoiner} ")
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(state.entryDate)
                        }
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            itemsIndexed(state.values) { index, item ->
                ValueItem(
                    value = item,
                    position = index + 1
                )
            }
        }
    }
}

@Composable
fun ValueItem(
    value: ValueUIModel,
    position: Int
) {
    Column {
        if(position > 1){
            HorizontalDivider()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = position.toString(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.defaultMinSize(minWidth = 40.dp),
                textAlign = TextAlign.Center
            )
            Text(value.name)
        }
    }
}

@Composable
@Preview
private fun PreviewEntryScreen() {
    NinetyValuesTheme {
        Surface {
            EntryScreen(
                state = EntryState(
                    entryDate = "wczoraj",
                    headerMessageDateJoiner = "ze",
                    values = listOf(
                        ValueUIModel(
                            id = 0,
                            name = "Druga"
                        ),
                        ValueUIModel(
                            id = 0,
                            name = "Pierwsza"
                        ),
                        ValueUIModel(
                            id = 0,
                            name = "Trzecia"
                        ),
                        ValueUIModel(
                            id = 0,
                            name = "Siódma"
                        ),
                    )
                )
            ) { }
        }
    }
}