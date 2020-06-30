package com.lodz.android.corekt.album

import android.net.Uri
import com.lodz.android.corekt.anko.getSize

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

    /** 文件夹的名称 */
    var name = ""

    /** 文件夹里包含的图片 */
    var picList: ArrayList<PicInfo> = ArrayList()

    /** 是否所有图片 */
    fun isAllPicture(): Boolean = dir.isEmpty()

    /** 添加图片[info] */
    internal fun addPicInfo(info: PicInfo) {
        picList.add(info)
    }
    /** 添加图片[info] */
    internal fun addPicInfo(infos: List<PicInfo>) {
        picList.addAll(infos)
    }

    /** 文件夹图片的数量 */
    fun getCount(): Int = picList.getSize()

    /** 文件夹封面图片 */
    fun getCoverPicInfo(): PicInfo = if (picList.size > 0) picList[0] else PicInfo("", Uri.EMPTY)
}