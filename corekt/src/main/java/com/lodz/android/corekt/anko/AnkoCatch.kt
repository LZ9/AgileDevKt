package com.lodz.android.corekt.anko

/**
 * try catch扩展类
 * @author zhouL
 * @date 2026/2/10
 */

/** try代码块[tryBlock]，catch处理代码块[catchBlock] */
fun <T> tryCatchOrNull(tryBlock: () -> T?, catchBlock: () -> T?): T? =
    try {
        tryBlock.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        catchBlock.invoke()
    }

/** try代码块[tryBlock]，抛异常直接返回null */
fun <T> tryCatchNull(tryBlock: () -> T?): T? = tryCatchOrNull(tryBlock) { null }

/** try代码块[tryBlock]，catch处理代码块[catchBlock] */
fun <T> tryCatch(tryBlock: () -> T, catchBlock: () -> T): T = tryCatchOrNull(tryBlock, catchBlock)!!

/** try代码块[tryBlock]，抛异常直接返回当前默认值 */
fun <T> T.tryCatch(tryBlock: () -> T): T = tryCatch(tryBlock) { this }

