package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.pandora.picker.file.PickerBean
import com.lodz.android.pandora.picker.file.pick.phone.PhonePickerActivity

/**
 * 手机文件选择
 * @author zhouL
 * @date 2022/5/11
 */
class DocumentPickerBuilder(private val pickerBean: PickerBean<DocumentWrapper>) : PickerBuilder<DocumentWrapper>(pickerBean){

    override fun startActivity(context: Context, flags: List<Int>?) {
        PhonePickerActivity.start(context, pickerBean, flags)
    }
}