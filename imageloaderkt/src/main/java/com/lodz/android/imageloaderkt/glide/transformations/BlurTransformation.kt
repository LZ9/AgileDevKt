package com.lodz.android.imageloaderkt.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.lodz.android.imageloaderkt.utils.blur.FastBlur
import com.lodz.android.imageloaderkt.utils.blur.RSBlur
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

class BlurTransformation(radius: Int = MAX_RADIUS, sampling: Int = DEFAULT_DOWN_SAMPLING) : BitmapTransformation() {

    private val VERSION = 1
    private val ID = "jp.wasabeef.glide.transformations.BlurTransformation.$VERSION"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    companion object {
        private val MIN_RADIUS = 1
        private val MAX_RADIUS = 25
        private val DEFAULT_DOWN_SAMPLING = 1
    }

    private var mRadius: Int
    private val mSampling: Float

    init {
        this.mRadius = radius
        if (radius < MIN_RADIUS) {
            mRadius = MIN_RADIUS
        }
        if (radius > MAX_RADIUS) {
            mRadius = MAX_RADIUS
        }
        this.mSampling = sampling.toFloat()
    }

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / mSampling.toInt()
        val scaledHeight = height / mSampling.toInt()

        val bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.scale(1 / mSampling, 1 / mSampling)
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                return RSBlur.blur(context, bitmap, mRadius)!!
            } catch (e: Exception) {
                e.printStackTrace()
                return FastBlur.blur(bitmap, mRadius, true)!!
            }
        } else {
            return FastBlur.blur(bitmap, mRadius, true)!!
        }
    }

    override fun toString() = "BlurTransformation(radius=$mRadius, sampling=$mSampling)"

    override fun equals(other: Any?) = other is BlurTransformation

    override fun hashCode() = ID.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}