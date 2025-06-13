package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import com.rafalskrzypczyk.ninetyvalues.ui.composables.ConfirmationDialog
import com.rafalskrzypczyk.ninetyvalues.ui.composables.NavigationTopBar
import sh.calvin.reorderable.ReorderableCollectionItemScope
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
        val reorderedValues = state.valuesToReorder.toMutableList().apply {
            add(to.index - 1, removeAt(from.index - 1))
        }
        onEvent.invoke(NewEntryUIEvents.OnValuesReordered(reorderedValues))
    }

    LaunchedEffect(Unit) {
        onEvent.invoke(NewEntryUIEvents.LoadInitialValues)
    }

    LaunchedEffect(state.newEntryId) {
        if(state.newEntryId != null) { onNavigateToSavedEntry(state.newEntryId) }
    }

    LaunchedEffect(state.selectingStep) {
        lazyListState.animateScrollToItem(0)
    }

    Scaffold (
        topBar = {
            Column {
                NavigationTopBar(
                    title = stringResource(R.string.title_new_entry),
                    onNavigateBack = onNavigateBack,
                    actions = {
                        if (state.submitAllowed) {
                            IconButton(onClick = {
                                if (state.confirmationRequired) showConfirmDialog.value = true
                                else onEvent.invoke(NewEntryUIEvents.OnSubmit)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = stringResource(R.string.ic_desc_submit)
                                )
                            }
                        }
                    }
                )
                AnimatedVisibility(visible = state.showProgressBar) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = { state.selectedItems.toFloat() / state.selectingLimit },
                        drawStopIndicator = {}
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.new_entry_header),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Text(
                    text = stringResource(state.selectingStep?.description ?: R.string.level_desc_finalization),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            if(state.orderingMode == OrderingMode.DRAGGING) {
                items(state.valuesToReorder, key = { it.id }) { item ->
                    ReorderableItem(
                        state = reorderableLazyListState,
                        key = item.id
                    ) { isDragging ->
                        OrderableValueItem(
                            scope = this,
                            item = item,
                            isDragging = isDragging,
                        )
                    }
                }
            } else {
                items(state.valuesToSelect, key = { it.id }) { item ->
                    SelectableValueItem(
                        item = item,
                        selectedColor = state.selectingStep?.color ?: Color.Transparent,
                        onItemSelected = { id -> onEvent.invoke(NewEntryUIEvents.OnValueSelected(id)) },
                        onItemDeselected = { id -> onEvent.invoke(NewEntryUIEvents.OnValueDeselected(id)) },
                        newSelectionsEnabled = state.orderingMode == OrderingMode.SELECTING
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
    scope: ReorderableCollectionItemScope,
    item: Value,
    isDragging: Boolean,
) {
    val elevation by animateDpAsState(if(isDragging) 16.dp else 0.dp, label = "dragElevation")
    val alpha by animateFloatAsState(targetValue = if (isDragging) 0.9f else 1f, label = "dragAlpha")
    val scale by animateFloatAsState(targetValue = if (isDragging) 1.1f else 1f, label = "dragScale")

    Surface(
        shadowElevation = elevation,
        modifier = with(scope) {
            Modifier
                .longPressDraggableHandle()
                .alpha(alpha)
                .scale(scale)
        }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = stringResource(R.string.ic_desc_draggable_item),
                modifier = with(scope) {
                    Modifier.draggableHandle()
                }
            )
        }
    }
}

@Composable
fun SelectableValueItem(
    item: ValueSelectableUIModel,
    selectedColor: Color,
    onItemSelected: (Long) -> Unit,
    onItemDeselected: (Long) -> Unit,
    newSelectionsEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge
        )
        AnimatedVisibility(
            visible = newSelectionsEnabled || item.isSelected,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(if (item.isSelected) selectedColor else Color.Transparent)
                    .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                    .clickable {
                        if (!(newSelectionsEnabled || item.isSelected)) return@clickable
                        if (item.isSelected) onItemDeselected(item.id) else onItemSelected(item.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (item.isSelected) {
                    Text(text = item.position.toString())
                }
            }
        }
    }
}