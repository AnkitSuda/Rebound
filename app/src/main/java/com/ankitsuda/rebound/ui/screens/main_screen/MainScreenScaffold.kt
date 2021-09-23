package com.ankitsuda.rebound.ui


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.utils.cmprs
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt


/**
 * Skeleton of sliding panel and main content
 *
 * @param swipeableState State of sliding panel
 * @param onPanelTopHeightChange Returns panel top height
 * @param bottomBar Bottom Nav Bar component
 * @param panel Panel main contents
 * @param panelTopCommon Common component of top panel views when fading between states
 * @param panelTopCollapsed Collapsed component of panel top
 * @param panelTopExpanded Expanded component of panel top
 * @param mainBody Main content component
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreenScaffold(
    modifier: Modifier,
    swipeableState: SwipeableState<Int>,
    onPanelTopHeightChange: (Int) -> Unit = {},
    bottomBar: @Composable () -> Unit,
    panel: @Composable () -> Unit,
    panelTopCommon: @Composable () -> Unit,
    panelTopCollapsed: @Composable () -> Unit,
    panelTopExpanded: @Composable () -> Unit,
    mainBody: @Composable () -> Unit,
) {
    val coroutine = rememberCoroutineScope()

    var panelTopHeight by remember() {
        mutableStateOf(1)
    }

    var panelFullHeight by remember {
        mutableStateOf(0)
    }

    var bottomBarHeight by remember {
        mutableStateOf(0)
    }

    var statusBarHeight by remember {
        mutableStateOf(0)
    }

    onPanelTopHeightChange(panelTopHeight)

    var panelHiddenContentHeight = panelFullHeight - panelTopHeight
    val anchors = mapOf(
        (panelHiddenContentHeight - bottomBarHeight).toFloat() to 0,
        0f to 1
    ) // Maps anchor points (in px) to states

    val newOffsetY = swipeableState.offset.value.roundToInt()


    val outOf1 =
        1f - ((newOffsetY).toFloat() / (panelHiddenContentHeight - bottomBarHeight).toFloat()).coerceIn(
            0f,
            1f
        )

    SubcomposeLayout(
        modifier = modifier
    ) { constraints ->

        // ------------------ StatusBar STARTS
        val statusBarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
        val statusBarPlaceables = subcompose(MainScreenScaffoldContent.StatusBar) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsHeight()
                    .background(ReboundTheme.colors.background)
            )
        }.map { it.measure(statusBarConstraints) }

        statusBarHeight = statusBarPlaceables.maxOfOrNull { it.height } ?: 0
        // ------------------ StatusBar ENDS

        // ------------------ BottomBar STARTS
        val bottomBarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
        val bottomBarPlaceables = subcompose(MainScreenScaffoldContent.BottomBar) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .alpha((1f - outOf1).coerceIn(0.7f, 1f))
            ) {
                bottomBar()
            }
        }.map { it.measure(bottomBarConstraints) }

        bottomBarHeight = bottomBarPlaceables.maxOfOrNull { it.height } ?: 0
        // ------------------ BottomBar ENDS

        // ------------------ Panel STARTS
        val panelConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = constraints.maxHeight - statusBarHeight
        )
        val panelPlaceables = subcompose(MainScreenScaffoldContent.HashtagsPanel) {
            val cornerRadius = (12 - (12 * (1f - (2f - (outOf1 * 2)).coerceIn(0f, 1f))))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ ->
                            // Automaticly toggle the state when user lifts the finger
                            // when drag is reached 0.1f FractionalThreshold
                            FractionalThreshold(0.1f)
                        },
                        resistance = null, // passing null so the panel doesn't go beyond the specified height
                        orientation = Orientation.Vertical
                    )
                    .nestedScroll(
                        // We are using NestedScrollConnection to make panel swipeable when
                        // user scrolls inside the panel
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                return if (delta < 0) {
                                    // User is moving the finger upwards. If the gesture goes in that direction,
                                    // we’re scrolling either the draggable composable or the scrollable inner content.
                                    Offset(0f, swipeableState.performDrag(delta))
                                } else {
                                    // User is scrolling down. We can ignore this and pass all
                                    // the consumable space down to the child
                                    Offset.Zero
                                }
                            }

                            override fun onPostScroll(
                                consumed: Offset,
                                available: Offset,
                                source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                // if the list has finished scrolling, we will pass all the leftover space
                                // to performDrag that will drag if necessary.
                                return Offset(0f, swipeableState.performDrag(delta))
                            }

                            // Same as preScroll but this time we handle the fling
                            override suspend fun onPreFling(available: Velocity): Velocity {
                                return if (available.y < 0 && swipeableState.currentValue == 0) {
                                    swipeableState.performFling(available.y)
                                    available
                                } else {
                                    Velocity.Zero
                                }
                            }

                            // Same as postScroll but this time we handle the fling
                            override suspend fun onPostFling(
                                consumed: Velocity,
                                available: Velocity
                            ): Velocity {
                                swipeableState.performFling(velocity = available.y)
                                return super.onPostFling(consumed, available)
                            }
                        }),
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = cornerRadius.dp, topEnd = cornerRadius.dp)
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                ) {


                    Box(

                        modifier = Modifier
                            .onGloballyPositioned { constraints ->
                                panelTopHeight = constraints.size.height
                            }
                            .clickable(
                                indication = null, // passing null in indication so there won't be any ripple effect
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutine.launch {
                                    swipeableState.animateTo(if (swipeableState.currentValue == 0) 1 else 0)
                                }

                            },
                    ) {

                        // Using additional Box so we can set alpha without recomposing the panelTopExpanded
                        (1f - (2f - (outOf1 * 2)).coerceIn(0f, 1f)).let { alpha ->
                            if (alpha > 0f) {
                                Box(modifier = Modifier.alpha(alpha)) {
                                    panelTopExpanded()
                                }
                            }
                        }

                        // Using additional Box so we can set alpha without recomposing the panelTopCollapsed
                        (1f - (outOf1 * 2).coerceIn(0f, 1f)).let { alpha ->
                            if (alpha > 0f) {
                                Box(modifier = Modifier.alpha(alpha)) {
                                    panelTopCollapsed()
                                }
                            }
                        }

                        // This panelTopCommon is always visible regard less of panel state 
                        panelTopCommon()

                    }

                    // Main panel contents
                    panel()
                }
            }
        }.map { it.measure(panelConstraints) }

        panelFullHeight = panelPlaceables.maxOfOrNull { it.height } ?: 0

        // ------------------ Panel ENDS

        val bodyConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = constraints.maxHeight - bottomBarHeight - panelTopHeight
        )

        val bodyPlaceables = subcompose(MainScreenScaffoldContent.Body) {
            mainBody()
        }.map { it.measure(bodyConstraints) }


        val panelScrimConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = constraints.maxHeight - bottomBarHeight
        )

        val panelScrimPlaceables = subcompose(MainScreenScaffoldContent.PanelScrim) {
            if (outOf1 > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .alpha(outOf1)
                        .background(color = Color.Black.copy(alpha = 0.32f))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    coroutine.launch {
                                        swipeableState.animateTo(0)
                                    }
                                }
                            )
                        },
                )
            }
        }.map { it.measure(panelScrimConstraints) }


        val width = constraints.maxWidth
        val height = constraints.maxHeight

        layout(width, height) {
            bodyPlaceables.forEach {
                it.place(
                    0,
                    0
                )
            }

            panelScrimPlaceables.forEach {
                it.place(
                    0,
                    0
                )
            }

            panelPlaceables.forEach {
                it.place(
                    0,
                    height - (panelFullHeight - newOffsetY)
                )
            }

            bottomBarPlaceables.forEach {
                it.place(
                    0,
                    height - (bottomBarHeight - bottomBarHeight * outOf1).roundToInt()

                )
            }

            statusBarPlaceables.forEach {
                it.place(
                    0,
                    -(statusBarHeight - statusBarHeight * (1f - (2f - (outOf1 * 2)).coerceIn(
                        0f,
                        1f
                    ))).roundToInt()
                )
            }

        }
    }
}

private enum class MainScreenScaffoldContent {
    Body, BottomBar, HashtagsPanel, StatusBar, PanelScrim
}
