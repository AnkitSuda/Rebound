package com.ankitsuda.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import androidx.compose.material.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.ui.theme.ReboundTheme
//import com.ankitsuda.rebound.utils.TopBarAlignment
//import com.ankitsuda.rebound.utils.isDark
//import com.ankitsuda.rebound.utils.lighterOrDarkerColor
import kotlin.math.max

/**
 * TopBar, usage as a toolbar
 * @param title Title of the screen
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    statusBarEnabled: Boolean = true,
    leftIconBtn: (@Composable () -> Unit)? = null,
    strictLeftIconAlignToStart: Boolean = true,
    alignRightIconToLeftWhenTitleAlignIsNotCenter: Boolean = false,
    rightIconBtn: (@Composable () -> Unit)? = null,
    elevationEnabled: Boolean = true,
    viewModel: TopBarViewModel = hiltViewModel()
) {

    var startBoxWidth by remember {
        mutableStateOf(64.dp)
    }
    var endBoxWidth by remember {
        mutableStateOf(64.dp)
    }


//    val titleAlignment by viewModel.titleAlignment.collectAsState(initial = TopBarAlignment.CENTER)
//    val backgroundColor by viewModel.backgroundColor.collectAsState(initial = ReboundTheme.colors.background)
//    val contentColor by viewModel.contentColor.collectAsState(initial = ReboundTheme.colors.onBackground)
//    val elevation by viewModel.elevation.collectAsState(initial = 2)

    val backgroundColor = ReboundTheme.colors.background
    val contentColor = ReboundTheme.colors.onBackground
    val elevation = 2

    Surface(
        elevation = if (elevationEnabled) elevation.dp else 0.dp,
        modifier = Modifier.zIndex(elevation.toFloat()),
        color = backgroundColor
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            // Status bar
            if (statusBarEnabled) {
                Box(
                    modifier = Modifier
                        .statusBarsHeight()
                        .background(backgroundColor)
                )
            }

            // Main TopBar content
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),

                ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .onGloballyPositioned(with(LocalDensity.current) {
                            {
                                startBoxWidth = it.size.width.toDp()
                            }
                        })
                ) {
//                    if (titleAlignment == TopBarAlignment.CENTER || strictLeftIconAlignToStart) {
                    leftIconBtn?.let {
                        it()
                    }
//                    }
                }

                Text(
                    text = title,
                    style = ReboundTheme.typography.h5,
                    textAlign =
//                    when (titleAlignment) {
//                        TopBarAlignment.START -> TextAlign.Start
//                        TopBarAlignment.END -> TextAlign.End
//                        else -> TextAlign.Center
//                    },
                    TextAlign.Center,
                    modifier = Modifier
                        .align(
//                            when (titleAlignment) {
//                                TopBarAlignment.START -> Alignment.CenterStart
//                                TopBarAlignment.END -> Alignment.CenterEnd
//                                else -> Alignment.Center
//                            },
                            Alignment.Center
                        )
                        .padding(
                            start = /*if (titleAlignment != TopBarAlignment.CENTER) startBoxWidth + 8.dp else*/ 16.dp,
                            end = /*if (titleAlignment != TopBarAlignment.CENTER) endBoxWidth + 8.dp else*/ 16.dp
                        ),
                    color = contentColor,
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .onGloballyPositioned(with(LocalDensity.current) {
                            {
                                endBoxWidth = it.size.width.toDp()
                            }
                        }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (alignRightIconToLeftWhenTitleAlignIsNotCenter) {
                        rightIconBtn?.let {
                            Box() {
                                it()
                            }
                        }
                    }
//                    if (titleAlignment != TopBarAlignment.CENTER && !strictLeftIconAlignToStart) {
//                        leftIconBtn?.let {
//                            Box() {
//                                it()
//                            }
//                        }
//                    }
                    if (!alignRightIconToLeftWhenTitleAlignIsNotCenter) {
                        rightIconBtn?.let {
                            Box() {
                                it()
                            }
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit = {},
    backgroundColor: Color = ReboundTheme.colors.background,/*.lighterOrDarkerColor(0.09f)*/
    leftBackBtnEnabled: Boolean = true,
    rightClearBtnEnabled: Boolean = true,
) {

    var clearBtnWidth by remember {
        mutableStateOf(0)
    }

    Column(modifier = modifier.background(backgroundColor)) {
        Box(Modifier.statusBarsHeight())
        Row(
            modifier = Modifier
                .height(60.dp)
                .padding(start = 8.dp, end = 8.dp),
        ) {
            IconButton(onClick = {
                onBackClick()
            }, modifier = Modifier
                .align(Alignment.CenterVertically)
                .onGloballyPositioned {
                    clearBtnWidth = it.parentCoordinates!!.size.width
                }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = /*if (backgroundColor.isDark()) Color.White else*/ Color.Black,
                )
            }
            // TextField
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    textColor = /*if (backgroundColor.isDark()) Color.White else*/ Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f),
                value = value,
                singleLine = true,
                maxLines = 1,
                onValueChange = {
                    onValueChange(it)
                },
                placeholder = {
                    Text(
                        text = placeholder,
                    )
                },
            )
            AnimatedVisibility(
                visible = value.isNotEmpty(),
//                enter = slideIn({ IntOffset(clearBtnWidth, 0) }),
//                exit = slideOut({ IntOffset(clearBtnWidth, 0) }),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(onClick = {
                    onValueChange("")
                }, modifier = Modifier
                    .onGloballyPositioned {
                        clearBtnWidth = it.parentCoordinates!!.size.width
                    }) {
                    Icon(
                        tint = /*if (backgroundColor.isDark()) Color.White else*/ Color.Black,
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    }
}

@Composable
fun TopBarIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    enabled: Boolean = true,
    customTint: Color? = null,
    viewModel: TopBarViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(targetValue = if (enabled) 1f else 0.5f)
//    val contentColor by viewModel.contentColor.collectAsState(ReboundTheme.colors.onBackground)
    val contentColor = ReboundTheme.colors.onBackground

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.alpha(alpha)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = customTint ?: contentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun TopBarBackIconButton(onClick: () -> Unit) {
    TopBarIconButton(icon = Icons.Outlined.ArrowBack, title = "Back", onClick = onClick)
}