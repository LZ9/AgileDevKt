package com.lodz.android.agiledevkt.compose.navigation

/**
 * 页面路由表
 * @author zhouL
 * @date 2026/5/21
 */
object Route {

    const val Splash = "splash"

    const val Main = "Main"

    const val Detail = "detail/{id}"

    fun detail(id: String) = "detail/$id"
}