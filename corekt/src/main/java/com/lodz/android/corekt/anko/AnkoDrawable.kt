package com.lodz.android.corekt.anko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import androidx.core.graphics.drawable.toDrawable

/**
 * Drawable扩展类
 * Created by zhouL on 2018/7/2.
 */

/** 用颜色资源[color]创建Drawable */
fun Context.createColorDrawable(@ColorRes color: Int): ColorDrawable = getColorCompat(color).toDrawable()

/** 用颜色资源[color]创建Drawable */
fun View.createColorDrawable(@ColorRes color: Int): ColorDrawable = context.createColorDrawable(color)

/** 用颜色资源[color]创建Drawable */
fun RecyclerView.ViewHolder.createColorDrawable(@ColorRes color: Int): ColorDrawable = itemView.createColorDrawable(color)

/** 用颜色[color]创建Drawable */
fun Context.createColorIntDrawable(@ColorInt color: Int): ColorDrawable = color.toDrawable()

/** 用颜色[color]创建Drawable */
fun View.createColorIntDrawable(@ColorInt color: Int): ColorDrawable = context.createColorIntDrawable(color)

/** 用颜色[color]创建Drawable */
fun RecyclerView.ViewHolder.createColorIntDrawable(@ColorInt color: Int): ColorDrawable = itemView.createColorIntDrawable(color)

/** 用[bitmap]创建Drawable */
fun Context.createBitmapDrawable(bitmap: Bitmap): BitmapDrawable = bitmap.toDrawable(resources)

/** 用[bitmap]创建Drawable */
fun View.createBitmapDrawable(bitmap: Bitmap): BitmapDrawable = context.createBitmapDrawable(bitmap)

/** 用[bitmap]创建Drawable */
fun RecyclerView.ViewHolder.createBitmapDrawable(bitmap: Bitmap): BitmapDrawable = itemView.createBitmapDrawable(bitmap)

/** 用颜色资源[color]创建圆角为[allCornerSizePx]的Drawable */
fun Context.createCornerDrawable(@ColorRes color: Int, allCornerSizePx: Float): MaterialShapeDrawable =
    createCornerDrawable(color, getDefaultShapeModel(allCornerSizePx))

/** 用颜色资源[color]创建圆角为[allCornerSizePx]的Drawable */
fun View.createCornerDrawable(@ColorRes color: Int, allCornerSizePx: Float): MaterialShapeDrawable =
    context.createCornerDrawable(color, allCornerSizePx)

/** 用颜色资源[color]创建圆角为[allCornerSizePx]的Drawable */
fun RecyclerView.ViewHolder.createCornerDrawable(@ColorRes color: Int, allCornerSizePx: Float): MaterialShapeDrawable =
    itemView.createCornerDrawable(color, allCornerSizePx)

/** 用颜色资源[color]创建圆角模式为[shapeModel]的Drawable */
fun Context.createCornerDrawable(@ColorRes color: Int, shapeModel: ShapeAppearanceModel): MaterialShapeDrawable =
    MaterialShapeDrawable(shapeModel).apply {
        setTint(getColorCompat(color))
        paintStyle = Paint.Style.FILL
    }

/** 用颜色资源[color]创建圆角模式为[shapeModel]的Drawable */
fun View.createCornerDrawable(@ColorRes color: Int, shapeModel: ShapeAppearanceModel): MaterialShapeDrawable =
    context.createCornerDrawable(color, shapeModel)

/** 用颜色资源[color]创建圆角模式为[shapeModel]的Drawable */
fun RecyclerView.ViewHolder.createCornerDrawable(@ColorRes color: Int, shapeModel: ShapeAppearanceModel): MaterialShapeDrawable =
    itemView.createCornerDrawable(color, shapeModel)

/** 获取默认的样式配置模型，圆角大小[allCornerSizePx] */
private fun getDefaultShapeModel(allCornerSizePx: Float): ShapeAppearanceModel =
    ShapeAppearanceModel.builder()
        .setAllCorners(RoundedCornerTreatment())
        .setAllCornerSizes(allCornerSizePx)
        .build()