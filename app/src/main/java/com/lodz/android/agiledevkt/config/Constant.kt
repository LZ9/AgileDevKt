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
    const val CHN_FLAG_URL = "https://img2.baidu.com/it/u=4278094243,562324040&fm=26&fmt=auto&gp=0.jpg"
    /** 美国国旗图片路径 */
    const val USA_FLAG_URL = "https://img2.baidu.com/it/u=2257277844,2022007382&fm=26&fmt=auto&gp=0.jpg"
    /** 俄罗斯国旗图片路径 */
    const val RUS_FLAG_URL = "https://img0.baidu.com/it/u=1373883787,2984433663&fm=26&fmt=auto&gp=0.jpg"
    /** 日本国旗图片路径 */
    const val JPN_FLAG_URL = "https://img2.baidu.com/it/u=513426073,1461725008&fm=26&fmt=auto&gp=0.jpg"
    /** 韩国国旗图片路径 */
    const val KOR_FLAG_URL = "https://img2.baidu.com/it/u=598063319,3429544611&fm=26&fmt=auto&gp=0.jpg"
    /** 澳大利亚国旗图片路径 */
    const val AUS_FLAG_URL = "https://img2.baidu.com/it/u=3038062313,2931308920&fm=26&fmt=auto&gp=0.jpg"
    /** 乌克兰国旗图片路径 */
    const val UKR_FLAG_URL = "https://img2.baidu.com/it/u=2341583464,3462049343&fm=26&fmt=auto&gp=0.jpg"
    /** 朝鲜国旗图片路径 */
    const val PRK_FLAG_URL = "https://img0.baidu.com/it/u=3821907153,2547388015&fm=26&fmt=auto&gp=0.jpg"
    /** 巴西国旗图片路径 */
    const val BRA_FLAG_URL = "https://img0.baidu.com/it/u=3679865739,2663472988&fm=26&fmt=auto&gp=0.jpg"

    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SHIP_TYPE_YZCB, SHIP_TYPE_WZCB)
    annotation class ShipType
    /** 有证船舶 */
    const val SHIP_TYPE_YZCB = 0
    /** 无证船舶 */
    const val SHIP_TYPE_WZCB = 1

}