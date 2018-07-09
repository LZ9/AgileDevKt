package com.lodz.android.imageloaderkt.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
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

@Deprecated("Use RequestOptions.circleCrop()")
class CropCircleTransformation : BitmapTransformation() {

    private val VERSION = 1
    private val ID = "jp.wasabeef.glide.transformations.CropCircleTransformation.$VERSION"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int) = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight)

    override fun toString() = "CropCircleTransformation()"

    override fun equals(other: Any?) = other is CropCircleTransformation

    override fun hashCode() = ID.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}