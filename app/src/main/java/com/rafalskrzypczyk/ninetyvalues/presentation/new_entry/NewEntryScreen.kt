package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import com.rafalskrzypczyk.ninetyvalues.ui.composables.ConfirmationDialog
import com.rafalskrzypczyk.ninetyvalues.ui.composables.NavigationTopBar
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    state: NewEntryState,
    onEvent: (NewEntryUIEvents) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSavedEntry: (Long) -> Unit
) {
    val showConfirmDialog = remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val reorderedValues = state.values.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        onEvent.invoke(NewEntryUIEvents.OnValuesReordered(reorderedValues))
    }

    LaunchedEffect(Unit) {
        onEvent.invoke(NewEntryUIEvents.LoadInitialValues)
    }

    LaunchedEffect(state.newEntryId) {
        if(state.newEntryId != null) { onNavigateToSavedEntry(state.newEntryId) }
    }

    Scaffold (
        topBar = {
            NavigationTopBar(
                title = stringResource(R.string.title_new_entry),
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { showConfirmDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.ic_desc_submit)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            item {
//                Text(
//                    text = stringResource(R.string.new_entry_header),
//                    textAlign = TextAlign.Center
//                )
//            }
            items(state.values, key = { it.id }) { item ->
                ReorderableItem(
                    state = reorderableLazyListState,
                    key = item.id
                ) { isDragging ->
                    val elevation by animateDpAsState(if(isDragging) 16.dp else 0.dp, label = "dragElevation")
                    val alpha by animateFloatAsState(targetValue = if (isDragging) 0.9f else 1f, label = "dragAlpha")
                    val scale by animateFloatAsState(targetValue = if (isDragging) 1.1f else 1f, label = "dragScale")

                    OrderableValueItem(
                        item = item,
                        elevation = elevation,
                        modifier = Modifier
                            .longPressDraggableHandle()
                            .alpha(alpha)
                            .scale(scale)
                    )
                }
            }
        }
    }

    if(showConfirmDialog.value){
        ConfirmationDialog(
            onDismissRequest = { showConfirmDialog.value = false },
            onConfirmation = { onEvent.invoke(NewEntryUIEvents.SaveEntry) },
            dialogTitle = stringResource(R.string.title_confirm_save_entry),
            dialogText = stringResource(R.string.msg_confirm_save_entry)
        )
    }
}

@Composable
fun OrderableValueItem(
    modifier: Modifier,
    item: Value,
    elevation: Dp
) {
    Surface(
        shadowElevation = elevation,
        modifier = modifier.fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = stringResource(R.string.ic_desc_draggable_item)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}