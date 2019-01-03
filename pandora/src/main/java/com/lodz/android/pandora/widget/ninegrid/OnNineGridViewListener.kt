package com.lodz.android.pandora.widget.ninegrid

import android.content.Context
import android.widget.ImageView

/**
 * 九宫格接口
 * Created by zhouL on 2018/12/25.
 */
interface OnNineGridViewListener {

    /** 添加图片，可添加的数量[addCount] */
    fun onAddPic(addCount: Int)

    /** 展示图片，上下文[context]，数据[data]，控件[imageView] */
    fun onDisplayImg(context: Context, data: String, imageView: ImageView)

    /** 删除图片，数据[data]，位置[position] */
    fun onDeletePic(data: String, position: Int)

    /** 点击图片，数据[data]，位置[position] */
    fun onClickPic(data: String, position: Int)
}