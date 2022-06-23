package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.file.PickerBean

/**
 * 挑选指定网络图片
 * @author zhouL
 * @date 2022/5/11
 */
class ImageUrlPickerBuilder<VH : RecyclerView.ViewHolder>(val pickerBean: PickerBean<String, VH>) : PickerBuilder<String, VH>(pickerBean){


    override fun startActivity(context: Context, flags: List<Int>?) {


    }

}