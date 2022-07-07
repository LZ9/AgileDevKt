package com.lodz.android.corekt.file

/**
 * 选择器信息摘要
 * Created by zhouL on 2018/7/3.
 */
interface PickerInfo {

    /** 名称 */
    fun getName(): String

    /** 格式化文件大小 */
    fun getSizeFormatStr(): String

    /** 格式化音频时长 */
    fun getDurationFormatStr(): String

}