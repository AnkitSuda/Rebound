package com.ankitsuda.rebound.ui.components.calpose.model

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween

/**
 * Describes various properties used within Calpose
 *
 * @property changeMonthAnimation Set the animation type and properties when changing month
 *
 * @property changeMonthSwipeTriggerVelocity Define the velocity necessary to change month when swiping
 *
 */

data class CalposeProperties (
    val changeMonthAnimation: FiniteAnimationSpec<Float> = tween(durationMillis = 200),
    val changeMonthSwipeTriggerVelocity: Int = 300
)