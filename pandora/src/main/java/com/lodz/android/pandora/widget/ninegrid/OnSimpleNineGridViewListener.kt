package com.lodz.android.pandora.widget.ninegrid

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.pandora.picker.preview.vh.AbsImageView

/**
 * 简单实现的九宫格接口
 * Created by zhouL on 2018/12/25.
 */
interface OnSimpleNineGridViewListener<T : Any, VH : RecyclerView.ViewHolder> {

    /** 展示九宫格图片，上下文[context]，数据[data]，控件[imageView] */
    fun onDisplayNineGridImg(context: Context, data: PicInfo, imageView: ImageView)

    /** 展示选择器图片，上下文[context]，数据[data]，控件[imageView] */
    fun onDisplayPickerImg(context: Context, data: PicInfo, imageView: ImageView)

    /** 创建预览图片控件 */
    fun createImageView(): AbsImageView<T, VH>
}