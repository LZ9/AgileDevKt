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
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

class MaskTransformation(@DrawableRes private val maskId: Int) : BitmapTransformation() {
    private val VERSION = 1
    private val ID = "jp.wasabeef.glide.transformations.MaskTransformation.$VERSION"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    private val mPaint = Paint()

    init {
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
    }

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val mask = ContextCompat.getDrawable(context, maskId)

        val canvas = Canvas(bitmap)
        if (mask != null) {
            mask.setBounds(0, 0, width, height)
            mask.draw(canvas)
        }
        canvas.drawBitmap(toTransform, 0f, 0f, mPaint)
        return bitmap
    }

    override fun toString() = "MaskTransformation(maskId=$maskId)"

    override fun equals(other: Any?) = other is MaskTransformation

    override fun hashCode() = ID.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}
