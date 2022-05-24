package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import android.view.View
import com.lodz.android.pandora.picker.file.PickerBean

/**
 * 挑选指定资源id
 * @author zhouL
 * @date 2022/5/11
 */
class ResIdPickerBuilder<V : View>(val pickerBean: PickerBean<V, Int>) : PickerBuilder<V, Int>(pickerBean){


    override fun startActivity(context: Context, flags: List<Int>?) {


    }

}