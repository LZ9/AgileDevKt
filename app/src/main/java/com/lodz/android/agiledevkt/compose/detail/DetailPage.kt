package com.lodz.android.agiledevkt.compose.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * 详情页
 * @author zhouL
 * @date 2026/5/21
 */
@Composable
fun DetailPage(nav: NavController, id: String?) {
    Box(
        modifier= Modifier.fillMaxSize(),
        contentAlignment= Alignment.Center
    ){

        Text(
            text="详情页 id=$id",
            modifier = Modifier.clickable(onClick = {nav.popBackStack()})
        )

    }
}