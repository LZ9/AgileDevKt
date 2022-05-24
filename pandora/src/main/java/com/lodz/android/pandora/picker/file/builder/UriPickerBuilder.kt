package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import android.net.Uri
import android.view.View
import com.lodz.android.pandora.picker.file.PickerBean

/**
 * 挑选指定Uri
 * @author zhouL
 * @date 2022/5/11
 */
class UriPickerBuilder<V : View>(val pickerBean: PickerBean<V, Uri>) : PickerBuilder<V, Uri>(pickerBean){


    override fun startActivity(context: Context, flags: List<Int>?) {


    }

}