package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntryScreen(
    state: EntryState,
    onNavigateBack: () -> Unit
) {
    val positionComparisonVisible = remember { mutableStateOf(false) }

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
                if (state.isPositionDifferenceAvailable) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            positionComparisonVisible.value = positionComparisonVisible.value.not()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.CompareArrows,
                                contentDescription = stringResource(R.string.ic_desc_entry_compare_positions),
                                Modifier.rotate(90f)
                            )
                        }
                    }
                }
            }

            itemsIndexed(state.values) { index, item ->
                ValueItem(
                    value = item,
                    position = index + 1,
                    showPositionDifferences = positionComparisonVisible.value
                )
            }
        }
    }
}

@Composable
fun ValueItem(
    value: ValueUIModel,
    position: Int,
    showPositionDifferences: Boolean
) {
    Column {
        if(position > 1){
            HorizontalDivider()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    text = position.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.defaultMinSize(minWidth = 40.dp),
                    textAlign = TextAlign.Center
                )
                Text(value.name)
            }
            AnimatedVisibility(
                visible = showPositionDifferences,
                enter = slideInHorizontally { it * 2 },
                exit = slideOutHorizontally  { it * 2 }
            ){
                Row (
                    modifier = Modifier.width(50.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val differenceIcon = when {
                        value.positionChange > 0 -> Icons.Default.ArrowUpward
                        value.positionChange < 0 -> Icons.Default.ArrowDownward
                        else -> Icons.Default.Remove
                    }
                    val color = when {
                        value.positionChange == 0 -> Color.Gray
                        else -> Color.Green
                    }

                    if(value.positionChange != 0){
                        Text(
                            text = value.positionChange.absoluteValue.toString(),
                            color = color
                        )
                    }
                    Icon(
                        imageVector = differenceIcon,
                        contentDescription = stringResource(R.string.ic_desc_entry_position_difference),
                        tint = color
                    )
                }
            }
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
                            name = "Druga",
                            2
                        ),
                        ValueUIModel(
                            id = 0,
                            name = "Pierwsza",
                            -1
                        ),
                        ValueUIModel(
                            id = 0,
                            name = "Trzecia",
                            -1
                        ),
                        ValueUIModel(
                            id = 0,
                            name = "Siódma",
                            0
                        ),
                    ),
                    isPositionDifferenceAvailable = true
                )
            ) { }
        }
    }
}