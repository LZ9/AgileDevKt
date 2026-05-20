package com.lodz.android.pandora.widget.progress.compose

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

/**
 * 将Drawable包装成Compose Painter
 * @author zhouL
 * @date 2026/5/20
 */
@Composable
fun rememberDrawablePainterCompat(drawable: Drawable): Painter {
    val drawableState = remember(drawable) { drawable.mutate() }
    return object : Painter() {
        override val intrinsicSize: Size
            get() = Size( drawableState.intrinsicWidth.toFloat(), drawableState.intrinsicHeight.toFloat() )

        override fun DrawScope.onDraw() {
            // 设置 drawable 的 bounds 对应 Compose Canvas 的大小
            drawableState.setBounds(0, 0, size.width.toInt(), size.height.toInt())

            // 使用底层 Canvas 绘制 Drawable
            drawContext.canvas.nativeCanvas.apply {
                drawableState.draw(this)
            }
        }
    }
}