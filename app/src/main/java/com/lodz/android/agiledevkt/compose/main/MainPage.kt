package com.lodz.android.agiledevkt.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.UserBean
import com.lodz.android.agiledevkt.compose.navigation.Route
import com.lodz.android.agiledevkt.compose.theme.ColorsCompose
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.anko.getStringCompat
import com.lodz.android.pandora.widget.base.compose.base.BaseContent
import com.lodz.android.pandora.widget.base.compose.base.BaseContentState
import com.lodz.android.pandora.widget.base.compose.base.Orientation
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBar
import com.lodz.android.pandora.widget.base.compose.titlebar.titleBarOptionCreate
import com.lodz.android.pandora.widget.index.compose.IndexBar
import com.lodz.android.pandora.widget.index.compose.IndexBarOptionCreate

/**
 * 主页
 * @author zhouL
 * @date 2026/5/21
 */
@Composable
fun MainPage(nav: NavController?) {
    val context = LocalContext.current

    /** 索引列表 */
    val indexList = arrayListOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    )

    /** 标题文字 */
    val titleStr = stringResource(R.string.main_title)

    /** 索引文字 */
    var mIndexText by remember { mutableStateOf("") }

    /** 是否显示索引提示 */
    var isShowIndexTips by remember { mutableStateOf(false) }


    val data = listOf(
        UserBean("张三", "1"),
        UserBean("李四", "2"),
        UserBean("王五", "3"),
        UserBean("李四", "4"),
        UserBean("王五", "5"),
        UserBean("李四", "6"),
        UserBean("王五", "7"),
        UserBean("李四", "8"),
        UserBean("王五", "9"),
        UserBean("李四", "10"),
        UserBean("王五", "11"),
        UserBean("李四", "12"),
        UserBean("王五", "13"),
        UserBean("李四", "14"),
        UserBean("王五", "15"),
        UserBean("李四", "16"),
        UserBean("王五", "17")
    )

    BaseContent(
        pageState = BaseContentState.Content,
        titleBar = {
            TitleBar(
                modifier = Modifier.fillMaxWidth(),
                option = context.titleBarOptionCreate {
                    titleText = context.getStringCompat(R.string.main_title)
                    isNeedBackBtn = false
                },
                areaRight = {
                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },// 关闭默认的点击高亮效果
                                indication = null,// 关闭默认的点击高亮效果
                                onClick = {
                                    context.toastShort("刷新列表")
                                }),
                        text = stringResource(R.string.main_change_mood),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            )
        }
    ) {

        ConstraintLayout(Modifier.fillMaxSize()) {
            val (list, tips, index) = createRefs()

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .constrainAs(list) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
            ) {
                items(data) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                nav?.navigate(Route.detail(it.pswd))
                            }
                    ) {
                        Text("${it.pswd} - ${it.account}")
                    }
                }
            }

            IndexBar(
                modifier = Modifier
                    .width(30.dp)
                    .constrainAs(index) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                indexList = indexList,
                option = IndexBarOptionCreate {
                    orientation = Orientation.Vertical
                    indexTextColor = ColorsCompose.Color_00a0e9
                    indexTextSize = 15.sp
                    padding = PaddingValues(top = 10.dp, bottom = 10.dp)
                    indexTextWeight = FontWeight.Normal
                    pressBackgroundColor = ColorsCompose.Color_11000000
                },
                onIndexSelected = { position, indexText ->
                    isShowIndexTips = true
                    mIndexText = indexText
                },
                onDragEnd = {
                    isShowIndexTips = false
                }
            )

            if (isShowIndexTips) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = ColorsCompose.Color_00a0e9,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .constrainAs(tips) {
                            centerTo(parent)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = mIndexText,
                        color = Color.White,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }

}