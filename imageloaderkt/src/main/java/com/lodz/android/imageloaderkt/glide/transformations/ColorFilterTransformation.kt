package com.lodz.android.imageloaderkt.glide.transformations

import android.content.Context
import android.graphics.*
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

class ColorFilterTransformation(private val color: Int) : BitmapTransformation() {

    private val VERSION = 1
    private val ID = "jp.wasabeef.glide.transformations.ColorFilterTransformation.$VERSION"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val config = if (toTransform.config != null) toTransform.config else Bitmap.Config.ARGB_8888
        val bitmap = pool.get(width, height, config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP))
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return bitmap
    }

    override fun toString() = "ColorFilterTransformation(color=$color)"

    override fun equals(other: Any?) = other is ColorFilterTransformation

    override fun hashCode() = ID.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}