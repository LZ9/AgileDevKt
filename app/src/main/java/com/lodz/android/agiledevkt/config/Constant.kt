package com.lodz.android.agiledevkt.config

import androidx.annotation.IntDef

/**
 * 常量
 * Created by zhouL on 2018/10/17.
 */
object Constant {
    /** 通知组id */
    const val NOTIFI_GROUP_ID = "g0001"
    /** 主频道id */
    const val NOTIFI_CHANNEL_MAIN_ID = "c0001"
    /** 下载频道id */
    const val NOTIFI_CHANNEL_DOWNLOAD_ID = "c0002"
    /** 服务频道id */
    const val NOTIFI_CHANNEL_SERVICE_ID = "c0003"

    /** 中国国旗图片路径 */
    const val CHN_FLAG_URL = "https://img1.baidu.com/it/u=2006918833,4231207381&fm=253&fmt=auto&app=138&f=PNG?w=750&h=500"
    /** 美国国旗图片路径 */
    const val USA_FLAG_URL = "https://img2.baidu.com/it/u=2102316100,3099358666&fm=253&fmt=auto&app=120&f=JPEG?w=950&h=500"
    /** 俄罗斯国旗图片路径 */
    const val RUS_FLAG_URL = "https://pic.rmb.bdstatic.com/bjh/5f2c618054136e982e18756b6f493e22.jpeg@h_1280"
    /** 日本国旗图片路径 */
    const val JPN_FLAG_URL = "https://img2.baidu.com/it/u=2888320840,3259222355&fm=253&fmt=auto&app=120&f=JPEG?w=450&h=338"
    /** 韩国国旗图片路径 */
    const val KOR_FLAG_URL = "https://i0.hdslb.com/bfs/article/cafcab2a33f62597797829db72683b6dac274e1c.png@942w_629h_progressive.webp"
    /** 澳大利亚国旗图片路径 */
    const val AUS_FLAG_URL = "http://t14.baidu.com/it/u=699605829,1144007213&fm=224&app=112&f=JPEG?w=499&h=259"
    /** 乌克兰国旗图片路径 */
    const val UKR_FLAG_URL = "http://t14.baidu.com/it/u=899130927,3830214176&fm=224&app=112&f=JPEG?w=500&h=357"
    /** 朝鲜国旗图片路径 */
    const val PRK_FLAG_URL = "https://img2.baidu.com/it/u=2790666678,1176459229&fm=253&fmt=auto&app=138&f=JPEG?w=650&h=324"
    /** 巴西国旗图片路径 */
    const val BRA_FLAG_URL = "https://img2.baidu.com/it/u=3244057817,564327279&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=250"

    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SHIP_TYPE_YZCB, SHIP_TYPE_WZCB)
    annotation class ShipType
    /** 有证船舶 */
    const val SHIP_TYPE_YZCB = 0
    /** 无证船舶 */
    const val SHIP_TYPE_WZCB = 1

    /** 图片列表 */
    val IMG_URLS = arrayOf(
        "http://hiphotos.baidu.com/zhidao/pic/item/d439b6003af33a87dd932ba4cd5c10385243b595.jpg",
        "https://img1.baidu.com/it/u=2990920892,3737411498&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
        "https://img2.baidu.com/it/u=3500173643,1534073302&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313",
        "https://img0.baidu.com/it/u=2301350592,2711679799&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=888",
        "https://img2.baidu.com/it/u=2543800850,477458790&fm=253&fmt=auto?w=220&h=146",
        "https://img1.baidu.com/it/u=2252278149,82077139&fm=253&fmt=auto&app=120&f=JPEG?w=658&h=411",
        "https://img0.baidu.com/it/u=2591643770,1203398355&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
        "http://www.cznews.gov.cn/newweb/d/file/zhuanti/jzfkgz/2021-08-05/e82c7f811539344b89c27c598113033e.jpg"
    )

    val TAB_NAME = arrayListOf(
        "Pinarello",
        "Look",
        "Specialized"
    )
}