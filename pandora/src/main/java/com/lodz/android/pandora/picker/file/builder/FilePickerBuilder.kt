package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.file.PickerBean
import java.io.File

/**
 * 挑选指定文件
 * @author zhouL
 * @date 2022/5/11
 */
class FilePickerBuilder(private val pickerBean: PickerBean<File>) : PickerBuilder<File>(pickerBean){

    override fun startActivity(context: Context, flags: List<Int>?) {


    }

}