package com.lodz.android.pandora.photopicker.picker

import android.net.Uri
import com.lodz.android.corekt.album.PicInfo

/**
 * 适配器数据实体
 * Created by zhouL on 2018/12/19.
 */
internal class PickerItemBean {
    /** 照片路径 */
    var info: PicInfo = PicInfo("", Uri.EMPTY)
    /** 是否选中 */
    var isSelected = false
}