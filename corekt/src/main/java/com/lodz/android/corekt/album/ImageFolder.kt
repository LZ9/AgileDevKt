package com.lodz.android.corekt.album

/**
 * 图片文件夹实体
 * Created by zhouL on 2018/11/5.
 */
class ImageFolder {

    /** 图片的文件夹路径 */
    var dir = ""
        set(value) {
            field = value
            if (value.contains("/")) {
                val lastIndexOf = dir.lastIndexOf("/")
                name = dir.substring(if (lastIndexOf + 1 < dir.length) lastIndexOf + 1 else lastIndexOf)
            }
        }
    /** 文件夹封面图片的路径 */
    var coverImgPath = ""
    /** 文件夹的名称 */
    var name = ""
    /** 文件夹图片的数量 */
    var count = 0
    /** 是否所有图片 */
    fun isAllPicture(): Boolean = dir.isEmpty()
}