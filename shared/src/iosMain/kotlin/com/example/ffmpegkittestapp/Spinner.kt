package com.example.ffmpegkittestapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal actual fun Spinner(expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
                            options: List<String>, onOptionChosen: (String) -> Unit, current: String) {
    DropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        Text(current, modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(5.dp)).background(Color.LightGray),
            textAlign = TextAlign.Center)
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach {
                DropdownMenuItem(onClick = { onOptionChosen(it) }) {
                    Text(it)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
internal fun DropdownMenuBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable DropdownMenuBoxScope.() -> Unit
) {
    val density = LocalDensity.current
    var width by remember { mutableStateOf(0) }
    var menuHeight by remember { mutableStateOf(0) }
    val coordinates = remember { Ref<LayoutCoordinates>() }

    val scope = remember(density, menuHeight, width) {
        object : DropdownMenuBoxScope {
            override fun Modifier.dropdownSize(matchTextFieldWidth: Boolean): Modifier {
                return with(density) {
                    heightIn(max = menuHeight.toDp()).let {
                        if (matchTextFieldWidth) {
                            it.width(width.toDp())
                        } else it
                    }
                }
            }
        }
    }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier.onGloballyPositioned {
            width = it.size.width
            coordinates.value = it
            menuHeight = 1920
        }.expandable(
            onExpandedChange = { onExpandedChange(!expanded) },
            menuLabel = "Dropdown Menu"
        ).focusRequester(focusRequester)
    ) {
        scope.content()
    }

    SideEffect {
        if (expanded) focusRequester.requestFocus()
    }
}

@ExperimentalMaterialApi
internal interface DropdownMenuBoxScope {

    fun Modifier.dropdownSize(
        matchTextFieldWidth: Boolean = true
    ): Modifier

    @Composable
    fun DropdownMenu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        val expandedStates = remember { MutableTransitionState(false) }
        expandedStates.targetState = expanded

        if (expandedStates.currentState || expandedStates.targetState) {
            val transformOriginState = remember { mutableStateOf(TransformOrigin.Center) }
            val density = LocalDensity.current
            val popupPositionProvider = DropdownMenuPositionProvider(
                DpOffset.Zero,
                density
            ) { parentBounds, menuBounds ->
                transformOriginState.value = calculateTransformOrigin(parentBounds, menuBounds)
            }

            Popup(
                onDismissRequest = onDismissRequest,
                popupPositionProvider = popupPositionProvider
            ) {
                DropdownMenuContent(
                    expandedStates = expandedStates,
                    transformOriginState = transformOriginState,
                    modifier = modifier.dropdownSize(),
                    content = content
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.expandable(
    onExpandedChange: () -> Unit,
    menuLabel: String
) = pointerInput(Unit) {
    forEachGesture {
        coroutineScope {
            awaitPointerEventScope {
                var event: PointerEvent
                do {
                    event = awaitPointerEvent(PointerEventPass.Initial)
                } while (
                    !event.changes.all { it.changedToUp() }
                )
                onExpandedChange.invoke()
            }
        }
    }
}.semantics {
    contentDescription = menuLabel // this should be a localised string
    onClick {
        onExpandedChange()
    }
}

@Composable
internal fun DropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    DropdownMenuItemContent(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}
