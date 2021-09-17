package com.ankitsuda.rebound.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@Composable
fun RadioGroupCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    onSelectionChange: (index: Int, value: String) -> Unit,
    items: List<String>,
    selected: String,
) {
    AppCard(modifier = modifier) {
        Column() {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = text)
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.caption,
                            color = Color(117, 117, 117)
                        )
                    }
                }

            }
            FlowRow(crossAxisSpacing = 8.dp, mainAxisSpacing = 8.dp, modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
                for (item in items) {
                    Row(modifier = Modifier.clickable(onClick = {
                            onSelectionChange(items.indexOf(item), item)
                        }, indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                    ) {
                        RadioButton(selected = item == selected, onClick = {
                            onSelectionChange(items.indexOf(item), item)
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.replaceFirstChar { it.uppercase() }
                        )
                    }
                }
            }
        }
    }
}