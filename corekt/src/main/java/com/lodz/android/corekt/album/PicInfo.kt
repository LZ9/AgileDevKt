package com.lodz.android.corekt.album

import android.net.Uri

/**
 * 图片信息实体
 * @author zhouL
 * @date 2020/6/22
 */
class PicInfo(
    var path: String,
    var uri: Uri
){
    override fun toString(): String {
        return "path : $path\nuri : $uri"
    }
}