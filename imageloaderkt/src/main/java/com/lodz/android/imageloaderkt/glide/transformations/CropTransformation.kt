package com.lodz.android.imageloaderkt.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

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

class CropTransformation : BitmapTransformation {

    private val VERSION = 1
    private val ID = "jp.wasabeef.glide.transformations.CropTransformation.$VERSION"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    enum class CropType { TOP, CENTER, BOTTOM }

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mCropType = CropType.CENTER

    constructor(width: Int, height: Int) : this(width, height, CropType.CENTER)

    constructor(width: Int, height: Int, cropType: CropType) : super() {
        this.mWidth = width
        this.mHeight = height
        this.mCropType = cropType
    }

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        mWidth = if (mWidth == 0) toTransform.width else mWidth
        mHeight = if (mHeight == 0) toTransform.height else mHeight

        val config = if (toTransform.config != null) toTransform.config else Bitmap.Config.ARGB_8888
        val bitmap = pool.get(mWidth, mHeight, config)
        bitmap.setHasAlpha(true)

        val scaleX = mWidth / toTransform.width.toFloat()
        val scaleY = mHeight / toTransform.height.toFloat()
        val scale = Math.max(scaleX, scaleY)

        val scaledWidth = scale * toTransform.width.toFloat()
        val scaledHeight = scale * toTransform.height.toFloat()
        val left = (mWidth - scaledWidth) / 2
        val top = getTop(scaledHeight)
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(toTransform, null, targetRect, null)
        return bitmap
    }

    private fun getTop(scaledHeight: Float) = when (mCropType) {
        CropType.TOP -> 0f
        CropType.CENTER -> (mHeight - scaledHeight) / 2
        CropType.BOTTOM -> mHeight - scaledHeight
    }

    override fun toString() = ("CropTransformation(width=$mWidth, height=$mHeight, cropType=$mCropType)")

    override fun equals(other: Any?) = other is CropTransformation

    override fun hashCode() = ID.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}