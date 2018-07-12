package com.lodz.android.imageloaderkt.glide.transformations

/**
 * Copyright (C) 2018 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

class RoundedCornersTransformation : BitmapTransformation {

    private val VERSION = 1
    private val ID = "jp.wasabeef.glide.transformations.RoundedCornersTransformation.$VERSION"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    enum class CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT,
        OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    private val mRadius: Float
    private val mDiameter: Float
    private val mMargin: Float
    private val mCornerType: CornerType

    constructor(radius: Int, margin: Int) : this(radius, margin, CornerType.ALL)

    constructor(radius: Int, margin: Int, cornerType: CornerType) : super() {
        this.mRadius = radius.toFloat()
        mDiameter = mRadius * 2.0f
        this.mMargin = margin.toFloat()
        this.mCornerType = cornerType
    }

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.setShader(BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return bitmap
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val right = width - mMargin
        val bottom = height - mMargin

        when (mCornerType) {
            CornerType.ALL -> canvas.drawRoundRect(RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint)
            CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, right, bottom)
            CornerType.TOP -> drawTopRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, right, bottom)
            CornerType.LEFT -> drawLeftRoundRect(canvas, paint, right, bottom)
            CornerType.RIGHT -> drawRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(canvas, paint, right, bottom)
            CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom)
            else -> canvas.drawRoundRect(RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint)
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint)
        canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
    }


    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom), paint)
        canvas.drawRect(RectF(right - mRadius, mMargin + mRadius, right, bottom), paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, mMargin + mDiameter, bottom - mRadius), paint)
        canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
    }

    private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom), paint)
        canvas.drawRect(RectF(right - mRadius, mMargin, right, bottom - mRadius), paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin + mRadius, right, bottom), paint)
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, right, bottom - mRadius), paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
    }

    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom), paint)
    }

    private fun drawOtherTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint)
    }

    private fun drawOtherTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint)
    }

    private fun drawOtherBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin + mRadius, right - mRadius, bottom), paint)
    }

    private fun drawOtherBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint)
    }

    private fun drawDiagonalFromTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin + mRadius, right - mDiameter, bottom), paint)
        canvas.drawRect(RectF(mMargin + mDiameter, mMargin, right, bottom - mRadius), paint)
    }

    private fun drawDiagonalFromTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint)
        canvas.drawRect(RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint)
    }

    override fun toString() = ("RoundedTransformation(radius=$mRadius, margin=$mMargin, diameter=$mDiameter, cornerType=${mCornerType.name})")

    override fun equals(other: Any?) = other is RoundedCornersTransformation

    override fun hashCode() = ID.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}