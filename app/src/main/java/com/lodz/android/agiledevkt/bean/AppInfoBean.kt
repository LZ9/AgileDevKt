package com.lodz.android.agiledevkt.bean

import com.lodz.android.corekt.anko.append

/**
 * 应用信息类
 * @author zhouL
 * @date 2020/6/14
 */
class AppInfoBean(
    /** 图标路径 */
    var imgUrl: String,
    /** 应用名称 */
    var appName: String,
    /** 应用描述 */
    var appDesc: String,
    /** 下载路径 */
    var downloadUrl: String
) {
    fun getSaveName(): String = appName.append(".apk")
}