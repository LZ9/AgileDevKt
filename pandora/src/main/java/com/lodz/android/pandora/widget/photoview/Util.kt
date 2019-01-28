package com.lodz.android.pandora.widget.photoview

import android.view.MotionEvent
import android.widget.ImageView

internal object Util {

    fun checkZoomLevels(minZoom: Float, midZoom: Float, maxZoom: Float) {
        if (minZoom >= midZoom) {
            throw IllegalArgumentException("Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value")
        } else if (midZoom >= maxZoom) {
            throw IllegalArgumentException("Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value")
        }
    }

    fun hasDrawable(imageView: ImageView): Boolean {
        return imageView.drawable != null
    }

    fun isSupportedScaleType(scaleType: ImageView.ScaleType?): Boolean {
        if (scaleType == null) {
            return false
        }
        if (scaleType == ImageView.ScaleType.MATRIX) {
            throw IllegalStateException("Matrix scale type is not supported")
        }
        return true
    }

    fun getPointerIndex(action: Int): Int {
        return action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    }
}