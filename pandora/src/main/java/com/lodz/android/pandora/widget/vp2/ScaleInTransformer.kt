package com.lodz.android.pandora.widget.vp2

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * 缩放的ViewPager2转换器
 * @author zhouL
 * @date 2020/1/1
 */
class ScaleInTransformer : ViewPager2.PageTransformer {

    companion object {
        const val DEFAULT_MIN_SCALE = 0.85f
        const val DEFAULT_CENTER = 0.5f
    }

    override fun transformPage(page: View, position: Float) {
        if (position == 0f){//page切换到正中间
            page.scaleY = 1f
            page.scaleX = 1f
            return
        }
        if (position > 0){ // 中间移向边路
            val scaleFactor = (1 - position) * (1 - DEFAULT_MIN_SCALE) + DEFAULT_MIN_SCALE
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
        } else {// 边路移向中间
            val scaleFactor = (1 + position) * (1 - DEFAULT_MIN_SCALE) + DEFAULT_MIN_SCALE
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
        }
    }
}