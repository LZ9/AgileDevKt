package com.lodz.android.pandora.widget.progress.compose

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lodz.android.corekt.anko.getAnimationDrawable
import kotlinx.coroutines.delay

/**
 * 自定义动画的Progress
 * @author zhouL
 * @date 2026/5/20
 */

/** 自定义动画的Progress，支持传入动画的资源编号[animationListRes] */
@Composable
fun AnimationProgress(modifier: Modifier, @DrawableRes animationListRes: Int) {
    AnimationProgress(modifier, LocalContext.current.getAnimationDrawable(animationListRes))
}

/** 自定义动画的Progress，支持传入动画的帧对象[frames] */
@Composable
fun AnimationProgress(modifier: Modifier, frames: List<Pair<Drawable, Int>>) {
    // 获取所有帧和对应的duration
    if (frames.isEmpty()) return
    // 当前帧的索引
    var currentFrameIndex by remember { mutableIntStateOf(0) }

    // 循环播放帧
    LaunchedEffect(frames) {
        while (true) {
            val duration = frames[currentFrameIndex].second
            delay(duration.toLong())
            currentFrameIndex = (currentFrameIndex + 1) % frames.size
        }
    }

    Box(modifier) {
        Image(
            painter = rememberDrawablePainterCompat(frames[currentFrameIndex].first),
            contentDescription = null
        )
    }
}
