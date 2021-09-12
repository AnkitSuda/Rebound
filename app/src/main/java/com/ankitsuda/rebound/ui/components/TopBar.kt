package com.ankitsuda.rebound.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntOffset

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
    rightIconBtn: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Status bar
        if (statusBarEnabled) {
            Box(modifier = Modifier.statusBarsHeight())
        }

        // Main TopBar content
        Box(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
        ) {
            leftIconBtn?.let {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    it()
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.onBackground,
            )

            rightIconBtn?.let {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    it()
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
    leftBackBtnEnabled: Boolean = true,
    rightClearBtnEnabled: Boolean = true,
) {

    var clearBtnWidth by remember {
        mutableStateOf(0)
    }

    Column(modifier = modifier.background(Color(248, 248, 248))) {
        Box(Modifier.statusBarsHeight())
        Row(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
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
                    contentDescription = "Back"
                )
            }
            // TextField
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
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
                enter = slideIn({ IntOffset(clearBtnWidth, 0) }),
                exit = slideOut({ IntOffset(clearBtnWidth, 0) }),
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
    tint: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(targetValue = if (enabled) 1f else 0.5f)

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.alpha(alpha)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun TopBarBackIconButton(onClick: () -> Unit) {
    TopBarIconButton(icon = Icons.Outlined.ArrowBack, title = "Back", onClick = onClick)
}