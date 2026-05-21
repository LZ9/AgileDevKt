package com.lodz.android.agiledevkt.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lodz.android.agiledevkt.compose.detail.DetailPage
import com.lodz.android.agiledevkt.compose.main.MainPage
import com.lodz.android.agiledevkt.compose.splash.SplashPage

/**
 * 导航组件
 * @author zhouL
 * @date 2026/5/21
 */
@Composable
fun AppNavHost() {

    val navController= rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash
    ) {
        composable (Route.Splash){
            SplashPage(navController)
        }

        composable(Route.Main){
            MainPage(navController)
        }

        composable(
            route=Route.Detail,
            arguments=listOf(navArgument("id"){ type= NavType.StringType })
        ){
            DetailPage(navController, it.arguments?.getString("id"))
        }

    }



}