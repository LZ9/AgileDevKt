package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.file.PickerBean

/**
 * 挑选指定资源id
 * @author zhouL
 * @date 2022/5/11
 */
class ResIdPickerBuilder(private val pickerBean: PickerBean<Int>) : PickerBuilder<Int>(pickerBean){


    override fun startActivity(context: Context, flags: List<Int>?) {


    }

}