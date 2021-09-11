/*
 * Copyright (c) 2021 onebone <me@onebone.me>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.ankitsuda.rebound.ui.components.collapsing_toolbar

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import timber.log.Timber
import kotlin.math.max

@Stable
class CollapsingToolbarScaffoldState(
    val toolbarState: CollapsingToolbarState,
    initialOffsetY: Int = 0,
) {
    val offsetY: Int
        get() = offsetYState.value

    internal val offsetYState = mutableStateOf(initialOffsetY)
}

private class CollapsingToolbarScaffoldStateSaver : Saver<CollapsingToolbarScaffoldState, Bundle> {
    override fun restore(value: Bundle): CollapsingToolbarScaffoldState {
        return CollapsingToolbarScaffoldState(
            CollapsingToolbarState(value.getInt("height", Int.MAX_VALUE)),
            value.getInt("offsetY", 0),
        )
    }

    override fun SaverScope.save(value: CollapsingToolbarScaffoldState): Bundle =
        Bundle().apply {
            putInt("height", value.toolbarState.height)
            putInt("offsetY", value.offsetY)
        }
}

@Composable
fun rememberCollapsingToolbarScaffoldState(
    toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState(),
): CollapsingToolbarScaffoldState {
    return rememberSaveable(toolbarState, saver = CollapsingToolbarScaffoldStateSaver()) {
        CollapsingToolbarScaffoldState(toolbarState)
    }
}

@Immutable
internal class FabPlacement(
    val isDocked: Boolean,
    val left: Int,
    val width: Int,
    val height: Int
)

@Composable
fun CollapsingToolbarScaffold(
    modifier: Modifier = Modifier.fillMaxSize(),
    state: CollapsingToolbarScaffoldState,
    scrollStrategy: ScrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
    toolbarModifier: Modifier = Modifier,
    toolbar: @Composable CollapsingToolbarScope.() -> Unit,
    onToolbarHeightChange: ((Int) -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    drawBodyUnderToolbar: Boolean = false,
    body: @Composable () -> Unit
) {
    val nestedScrollConnection = remember(scrollStrategy, state) {
        scrollStrategy.create(state.offsetYState, state.toolbarState)
    }

    val toolbarState = state.toolbarState

    Timber.d("${toolbarState.progress}")
    var originalToolbarHeight by with(LocalDensity.current) {
        remember {
            mutableStateOf(189)
        }
    }

    var didGotOriginalToolbarHeight by remember {
        mutableStateOf(false)
    }

    SubcomposeLayout(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
    ) { constraints ->
        val toolbarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )


        val toolbarPlaceables = subcompose(CollapsingToolbarScaffoldContent.Toolbar) {
            CollapsingToolbar(
                modifier = toolbarModifier,
                collapsingToolbarState = toolbarState
            ) {
                toolbar()
            }
        }.map { it.measure(toolbarConstraints) }

        val toolbarHeight = toolbarPlaceables.maxOfOrNull { it.height } ?: 0

//        if (!didGotOriginalToolbarHeight) {
//            originalToolbarHeight = toolbarHeight.toFloat()
//            didGotOriginalToolbarHeight = true
//        }

        onToolbarHeightChange?.invoke(toolbarHeight)

        val bodyConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight =
            if (scrollStrategy == ScrollStrategy.ExitUntilCollapsed)
                max(
                    0,
                    if (drawBodyUnderToolbar) (constraints.maxHeight + toolbarState.minHeight + toolbarHeight) else (constraints.maxHeight - toolbarState.minHeight)
                )
            else
                constraints.maxHeight + if (drawBodyUnderToolbar) toolbarHeight else 0
        )

        val bodyPlaceables = subcompose(CollapsingToolbarScaffoldContent.Body) {
            body()
        }.map { it.measure(bodyConstraints) }

        val width = max(
            toolbarPlaceables.maxOfOrNull { it.width } ?: 0,
            bodyPlaceables.maxOfOrNull { it.width } ?: 0
        ).coerceIn(constraints.minWidth, constraints.maxWidth)


        val height = max(
            toolbarHeight,
            bodyPlaceables.maxOfOrNull { it.height + if (drawBodyUnderToolbar) (toolbarHeight * 2) else 0 }
                ?: 0
        ).coerceIn(constraints.minHeight, constraints.maxHeight)

        val fabConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        val fabPlaceables = subcompose(
            CollapsingToolbarScaffoldContent.Fab,
            floatingActionButton
        ).mapNotNull { measurable ->
            measurable.measure(fabConstraints).takeIf { it.height != 0 && it.width != 0 }
        }

        val fabPlacement = if (fabPlaceables.isNotEmpty()) {
            val fabWidth = fabPlaceables.maxOfOrNull { it.width } ?: 0
            val fabHeight = fabPlaceables.maxOfOrNull { it.height } ?: 0
            // FAB distance from the left of the layout, taking into account LTR / RTL
            val fabLeftOffset = if (floatingActionButtonPosition == FabPosition.End) {
                if (layoutDirection == LayoutDirection.Ltr) {
                    constraints.maxWidth - 16.dp.roundToPx() - fabWidth
                } else {
                    16.dp.roundToPx()
                }
            } else {
                (constraints.maxWidth - fabWidth) / 2
            }

            FabPlacement(
                isDocked = false,
                left = fabLeftOffset,
                width = fabWidth,
                height = fabHeight
            )
        } else {
            null
        }
        val fabOffsetFromBottom = fabPlacement?.let {
            it.height + 16.dp.roundToPx()
        }

        layout(width, height) {
            bodyPlaceables.forEach {
                it.place(
                    0,
                    state.offsetY + if (drawBodyUnderToolbar) 0 else toolbarHeight
                )
            }

            toolbarPlaceables.forEach {
                it.place(0, state.offsetY)
            }

            fabPlacement?.let { placement ->
                fabPlaceables.forEach {
                    it.place(placement.left, constraints.maxHeight - fabOffsetFromBottom!!)
                }
            }

        }
    }
}

private enum class CollapsingToolbarScaffoldContent {
    Toolbar, Body, Fab
}
