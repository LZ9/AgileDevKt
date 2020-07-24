package com.lodz.android.agiledevkt.config

import androidx.annotation.Keep

/**
 * 地址配置
 * @author zhouL
 * @date 2019/3/22
 */
object UrlConfig {

    @Keep
    private const val Release = "http://www.baidu.com/" // 正式地址
    @Keep
    private const val Test = "http://192.168.5.204:38080/app/mock/32/yszz/" // 测试地址

    /** 正式环境 */
    const val BASE_URL = Test
}