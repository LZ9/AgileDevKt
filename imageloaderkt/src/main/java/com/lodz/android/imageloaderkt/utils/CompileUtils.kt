package com.lodz.android.imageloaderkt.utils

/**
 * 依赖判断帮助类
 * Created by zhouL on 2018/7/9.
 */
object CompileUtils {

    /** 通过完整包名[classFullName]判断指定的类是否存在 */
    @JvmStatic
    fun isClassExists(classFullName: String): Boolean {
        try {
            val c = Class.forName(classFullName)
            if (c != null) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

}