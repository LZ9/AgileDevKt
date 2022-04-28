package com.lodz.android.pandora.picker.file.pick

/**
 * 适配器数据实体
 * Created by zhouL on 2018/12/19.
 */
internal class DataWrapper<T> constructor(var data: T) {
    /** 是否选中 */
    var isSelected = false
}