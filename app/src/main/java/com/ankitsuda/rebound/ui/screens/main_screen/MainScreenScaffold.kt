package com.ankitsuda.rebound.ui


import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.R
import com.ankitsuda.rebound.utils.Utils
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random


/**
 * Skeleton of sliding panel and main content
 * Issue: Sliding doesn't work when panel has component with a scroll property, will fix it later.
 *
 * @param swipeableState State of sliding panel
 * @param onPanelTopHeightChange Returns panel top height
 * @param bottomBar Bottom Nav Bar component
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
    Timber.d("$newOffsetY , $panelFullHeight , $panelHiddenContentHeight , $panelTopHeight , $outOf1")

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
                    .background(Color.White)
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
            Box(Modifier.fillMaxWidth().alpha((1f- outOf1).coerceIn(0.5f, 1f))) {
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
            val cornerRadius = (12 - (12 * outOf1))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ ->
                            FractionalThreshold(0.1f)
                        },
                        resistance = null,
                        orientation = Orientation.Vertical
                    ),
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
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutine.launch {
                                    swipeableState.animateTo(if (swipeableState.currentValue == 0) 1 else 0)
                                }

                            },
                    ) {

                        Box(modifier = Modifier.alpha(outOf1)) {
                            panelTopExpanded()
                        }

                        Box(modifier = Modifier.alpha(1f - outOf1)) {
                            panelTopCollapsed()
                        }

                        panelTopCommon()

                    }



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


        val backDropConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = constraints.maxHeight - bottomBarHeight
        )

        val backDropPlaceables = subcompose(MainScreenScaffoldContent.BackDrop) {
            if (outOf1 > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .alpha(outOf1)
                        .background(color = colorResource(id = R.color.backdrop))
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
        }.map { it.measure(backDropConstraints) }


        val width = constraints.maxWidth
        val height = constraints.maxHeight

        layout(width, height) {
            bodyPlaceables.forEach {
                it.place(
                    0,
                    0
                )
            }

            backDropPlaceables.forEach {
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
                        .also { test ->
                            Timber.d("bottomBarPlaceables $test , $bottomBarHeight")
                        }
                )
            }

            statusBarPlaceables.forEach {
                it.place(
                    0,
                    -(statusBarHeight - statusBarHeight * outOf1).roundToInt()
                )
            }

        }
    }
}

private enum class MainScreenScaffoldContent {
    Body, BottomBar, HashtagsPanel, StatusBar, BackDrop
}
