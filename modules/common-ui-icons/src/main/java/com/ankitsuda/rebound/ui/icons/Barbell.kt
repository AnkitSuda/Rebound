package com.ankitsuda.rebound.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.Filled.Barbell: ImageVector
    get() {
        if (_barbell != null) {
            return _barbell!!
        }
        _barbell = Builder(name = "Barbell", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.052f, 2.6378f)
                lineToRelative(17.3101f, 17.3101f)
                lineToRelative(-1.4142f, 1.4142f)
                lineToRelative(-17.3101f, -17.3101f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0628f, 3.8201f)
                lineToRelative(1.731f, 1.731f)
                lineToRelative(-4.2426f, 4.2426f)
                lineToRelative(-1.731f, -1.731f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(18.4488f, 14.2062f)
                lineToRelative(1.731f, 1.731f)
                lineToRelative(-4.2426f, 4.2426f)
                lineToRelative(-1.731f, -1.731f)
                close()
            }
        }
        .build()
        return _barbell!!
    }

private var _barbell: ImageVector? = null
