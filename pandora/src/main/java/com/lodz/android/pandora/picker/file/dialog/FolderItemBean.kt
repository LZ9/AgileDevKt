package com.lodz.android.pandora.picker.file.dialog

import com.lodz.android.corekt.media.DocumentFolder

/**
 * 图片文件夹列表数据
 * Created by zhouL on 2018/12/18.
 */
class FolderItemBean {

    /** 图片文件夹 */
    var documentFolder: DocumentFolder? = null
    /** 是否选择 */
    var isSelected = false
}