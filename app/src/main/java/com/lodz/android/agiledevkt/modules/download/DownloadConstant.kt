package com.lodz.android.agiledevkt.modules.download

import com.lodz.android.agiledevkt.bean.AppInfoBean

/**
 * 下载常量
 * @author zhouL
 * @date 2020/6/14
 */
object DownloadConstant {

    fun getMarketApps(): ArrayList<AppInfoBean> {
        val list = ArrayList<AppInfoBean>()
        list.add(
            AppInfoBean(
                "https://pp.myapp.com/ma_icon/0/icon_10910_1590997128/96",
                "微信",
                "可以发语音、文字消息、表情、图片、视频30M流量可以收发上千条语音，省电省流量",
                "https://imtt.dd.qq.com/16891/apk/21E13DFA95A4ED2B7EFF16D907B91CE2.apk?fsname=com.tencent.mm_7.0.15_1680.apk&csr=1bbd"
            )
        )
        list.add(
            AppInfoBean(
                "https://pp.myapp.com/ma_icon/0/icon_6633_1591924672/96",
                "QQ",
                "随时随地收发好友和群消息，一触即达。",
                "https://imtt.dd.qq.com/16891/apk/55259F8EF9824AF1BF80106B0E00BCD1.apk?fsname=com.tencent.mobileqq_8.3.9_1424.apk&csr=1bbd"
            )
        )
        list.add(
            AppInfoBean(
                "https://pp.myapp.com/ma_icon/0/icon_6633_1591924672/96",
                "Fail",
                "测试",
                "https://imtt.dd.qq.com/16891/apk/asdwqweqw.apk"
            )
        )
        return list
    }
}