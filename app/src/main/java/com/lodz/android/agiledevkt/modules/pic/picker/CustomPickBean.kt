package com.lodz.android.agiledevkt.modules.pic.picker

import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.corekt.file.PickerInfo

/**
 * 自定义选择数据实体
 * @author zhouL
 * @date 2022/7/7
 */
class CustomPickBean(
    val name: String, //名称
    val url: String, //网络路径
    val type: Int, // 类型
    val document: DocumentWrapper? // 手机文件
) : PickerInfo {

    companion object {
        const val URL_IMG_TYPE = 1
        const val DOCUMENT_TYPE = 2
    }

    override fun getNameStr(): String = name

    override fun getSizeFormatStr(): String = ""

    override fun getDurationFormatStr(): String = ""
}