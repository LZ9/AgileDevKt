package com.lodz.android.agiledevkt.compose.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lodz.android.agiledevkt.bean.UserBean
import com.lodz.android.agiledevkt.compose.navigation.Route

/**
 * 主页
 * @author zhouL
 * @date 2026/5/21
 */
@Composable
fun MainPage(nav: NavController) {

    val data = listOf(
        UserBean("张三", "1"),
        UserBean("李四", "2"),
        UserBean("王五", "3")
    )

    LazyColumn {
        items(data) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clickable {
                        nav.navigate(Route.detail(it.pswd))
                    }
            ) {

                Text(it.account)

            }

        }

    }

}