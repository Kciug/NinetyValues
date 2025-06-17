package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import com.rafalskrzypczyk.ninetyvalues.ui.composables.ConfirmationDialog
import com.rafalskrzypczyk.ninetyvalues.ui.composables.NavigationTopBar
import kotlinx.coroutines.delay
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun NewEntryScreen(
    state: NewEntryState,
    onEvent: (NewEntryUIEvents) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSavedEntry: (Long) -> Unit
) {
    val showSaveConfirmationDialog = remember { mutableStateOf(false) }
    val showGuide = remember { mutableStateOf(true) }
    val showMainScreen = remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val reorderedValues = state.valuesToReorder.toMutableList().apply {
            add(to.index - 1, removeAt(from.index - 1))
        }
        onEvent.invoke(NewEntryUIEvents.OnValuesReordered(reorderedValues))
    }

    LaunchedEffect(state.newEntryId) {
        if(state.newEntryId != null) { onNavigateToSavedEntry(state.newEntryId) }
    }

    LaunchedEffect(showGuide.value) {
        if(!showGuide.value) {
            onEvent.invoke(NewEntryUIEvents.LoadInitialValues)
            delay(300)
            showMainScreen.value = true
        }
    }

    LaunchedEffect(state.step) {
        lazyListState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showMainScreen.value,
                enter = slideInVertically { -it } + fadeIn()
            ){
                Column {
                    NavigationTopBar(
                        title = stringResource(R.string.title_new_entry),
                        onNavigateBack = onNavigateBack
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
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        AnimatedVisibility(
            visible = showGuide.value,
            exit = fadeOut(),
            modifier = modifier.fillMaxSize()
        ) {
            NewEntryGuideScreen(
                onContinue = { showGuide.value = false },
                onNavigateBack = onNavigateBack
            )
        }

        AnimatedVisibility(
            visible = showMainScreen.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()){
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        if(state.orderingMode == OrderingMode.DRAGGING){
                            Text(
                                text = stringResource(R.string.level_desc_finalization),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        } else if (state.step > 1){
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                        append("${(state.step - 1) * state.selectingLimit} ")
                                    }
                                    append(stringResource(R.string.new_entry_header))
                                },
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.new_entry_header_hint),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                    if (state.orderingMode == OrderingMode.DRAGGING) {
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
                                onItemSelected = { id ->
                                    onEvent.invoke(
                                        NewEntryUIEvents.OnValueSelected(
                                            id
                                        )
                                    )
                                },
                                onItemDeselected = { id ->
                                    onEvent.invoke(
                                        NewEntryUIEvents.OnValueDeselected(
                                            id
                                        )
                                    )
                                },
                                newSelectionsEnabled = state.orderingMode == OrderingMode.SELECTING
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = state.submitAllowed,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Button(
                        onClick = {
                            if(state.isFinalizationStep) showSaveConfirmationDialog.value = true
                            else onEvent.invoke(NewEntryUIEvents.OnSubmit)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .zIndex(1f)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(50.dp),
                                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                    ) {
                        Text(
                            text = if(state.isFinalizationStep) stringResource(R.string.btn_save) else stringResource(R.string.btn_submit)
                        )
                    }
                }
            }
        }
    }

    if(showSaveConfirmationDialog.value){
        ConfirmationDialog(
            onDismissRequest = { showSaveConfirmationDialog.value = false },
            onConfirmation = {
                showSaveConfirmationDialog.value = false
                onEvent.invoke(NewEntryUIEvents.SaveEntry)
            },
            dialogTitle = stringResource(R.string.title_confirm_save_entry),
            dialogText = stringResource(R.string.msg_confirm_save_entry)
        )
    }
}

@Composable
fun NewEntryGuideScreen(
    onContinue: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = stringResource(R.string.ic_desc_guide)
        )
        Text(
            text = stringResource(R.string.new_entry_intro_header),
            textAlign = TextAlign.Center
        )
        Row {
            Icon(
                imageVector = Icons.Default.TripOrigin,
                contentDescription = stringResource(R.string.ic_desc_new_entry_guide_point)
            )
            Spacer(Modifier.width(16.dp))
            Text(stringResource(R.string.new_entry_intro_guide_1))
        }
        Row {
            Icon(
                imageVector = Icons.Default.TripOrigin,
                contentDescription = stringResource(R.string.ic_desc_new_entry_guide_point)
            )
            Spacer(Modifier.width(16.dp))
            Text(stringResource(R.string.new_entry_intro_guide_2))
        }
        Row {
            Icon(
                imageVector = Icons.Default.TripOrigin,
                contentDescription = stringResource(R.string.ic_desc_new_entry_guide_point)
            )
            Spacer(Modifier.width(16.dp))
            Text(stringResource(R.string.new_entry_intro_guide_3))
        }
        Text(
            text = stringResource(R.string.new_entry_intro_end),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(50.dp),
                    ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                    spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
        ) {
            Text(stringResource(R.string.btn_new_entry_start))
        }
        TextButton(
            onClick = onNavigateBack,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.btn_dismiss))
        }
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
                    .background(if (item.isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
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