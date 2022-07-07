package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.file.PickerBean

/**
 * 挑选指定Uri
 * @author zhouL
 * @date 2022/5/11
 */
class UriPickerBuilder(private val pickerBean: PickerBean<Uri>) : PickerBuilder<Uri>(pickerBean){


    override fun startActivity(context: Context, flags: List<Int>?) {


    }

}