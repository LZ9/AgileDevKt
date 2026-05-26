package com.lodz.android.agiledevkt.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.lodz.android.agiledevkt.bean.MainBean
import com.lodz.android.agiledevkt.compose.navigation.Route
import com.lodz.android.agiledevkt.compose.theme.ColorsCompose
import com.lodz.android.agiledevkt.modules.acache.ACacheTestActivity
import com.lodz.android.agiledevkt.modules.adsorb.AdsorbViewActivity
import com.lodz.android.agiledevkt.modules.annotation.AnnotationTestActivity
import com.lodz.android.agiledevkt.modules.api.ApiTestActivity
import com.lodz.android.agiledevkt.modules.array.ArrayTestActivity
import com.lodz.android.agiledevkt.modules.badge.BadgeTestActivity
import com.lodz.android.agiledevkt.modules.bitmap.BitmapTestActivity
import com.lodz.android.agiledevkt.modules.bluetooth.BluetoothTestActivity
import com.lodz.android.agiledevkt.modules.bottomsheet.BottomSheetsActivity
import com.lodz.android.agiledevkt.modules.bytes.BytesTestActivity
import com.lodz.android.agiledevkt.modules.camera.CameraMainActivity
import com.lodz.android.agiledevkt.modules.cardview.CardViewActivity
import com.lodz.android.agiledevkt.modules.collect.CollectActivity
import com.lodz.android.agiledevkt.modules.color.ColorAlphaTestActivity
import com.lodz.android.agiledevkt.modules.config.ConfigLayoutActivity
import com.lodz.android.agiledevkt.modules.contact.ContactTestActivity
import com.lodz.android.agiledevkt.modules.coordinator.CoordinatorTestActivity
import com.lodz.android.agiledevkt.modules.coroutines.CoroutinesActivity
import com.lodz.android.agiledevkt.modules.crash.CrashTestActivity
import com.lodz.android.agiledevkt.modules.datastore.DataStoreActivity
import com.lodz.android.agiledevkt.modules.date.DateActivity
import com.lodz.android.agiledevkt.modules.dialog.DialogActivity
import com.lodz.android.agiledevkt.modules.dialogfragment.DialogFragmentActivity
import com.lodz.android.agiledevkt.modules.dimensions.DimensionsActivity
import com.lodz.android.agiledevkt.modules.download.DownloadMarketActivity
import com.lodz.android.agiledevkt.modules.drawer.DrawerTestActivity
import com.lodz.android.agiledevkt.modules.extras.ExtrasTestActivity
import com.lodz.android.agiledevkt.modules.fglifecycle.FragmentLifecycleActivity
import com.lodz.android.agiledevkt.modules.file.FileTestActivity
import com.lodz.android.agiledevkt.modules.format.NumFormatTestActivity
import com.lodz.android.agiledevkt.modules.hole.HoleTestActivity
import com.lodz.android.agiledevkt.modules.idcard.IdcardTestActivity
import com.lodz.android.agiledevkt.modules.image.GlideActivity
import com.lodz.android.agiledevkt.modules.info.InfoTestActivity
import com.lodz.android.agiledevkt.modules.json.JsonTestActivity
import com.lodz.android.agiledevkt.modules.keyboard.KeyboardTestActivity
import com.lodz.android.agiledevkt.modules.location.LocationTestActivity
import com.lodz.android.agiledevkt.modules.menubar.MenuBarTestActivity
import com.lodz.android.agiledevkt.modules.mvc.MvcDemoActivity
import com.lodz.android.agiledevkt.modules.mvp.MvpDemoActivity
import com.lodz.android.agiledevkt.modules.mvvm.MvvmDemoActivity
import com.lodz.android.agiledevkt.modules.notification.NotificationActivity
import com.lodz.android.agiledevkt.modules.phonefile.PhoneFileActivity
import com.lodz.android.agiledevkt.modules.pic.PicActivity
import com.lodz.android.agiledevkt.modules.progressdialog.ProgressDialogActivity
import com.lodz.android.agiledevkt.modules.reflect.ReflectActivity
import com.lodz.android.agiledevkt.modules.result.ResultContractsCaseActivity
import com.lodz.android.agiledevkt.modules.rv.anim.AnimRvActivity
import com.lodz.android.agiledevkt.modules.rv.binder.BinderRvActivity
import com.lodz.android.agiledevkt.modules.rv.decoration.DecorationRvActivity
import com.lodz.android.agiledevkt.modules.rv.drag.DragRvActivity
import com.lodz.android.agiledevkt.modules.rv.head.HeadFooterRvActivity
import com.lodz.android.agiledevkt.modules.rv.loadmore.RefreshLoadMoreMainActivity
import com.lodz.android.agiledevkt.modules.rv.snap.SnapRvActivity
import com.lodz.android.agiledevkt.modules.rv.swipe.SwipeRvActivity
import com.lodz.android.agiledevkt.modules.rv.tree.TreeRvActivity
import com.lodz.android.agiledevkt.modules.rxjava.completable.RxCompletableActivity
import com.lodz.android.agiledevkt.modules.rxjava.flowable.RxFlowableActivity
import com.lodz.android.agiledevkt.modules.rxjava.maybe.RxMaybeActivity
import com.lodz.android.agiledevkt.modules.rxjava.observable.RxObservableActivity
import com.lodz.android.agiledevkt.modules.rxjava.single.RxSingleActivity
import com.lodz.android.agiledevkt.modules.rxjava.utils.RxUtilsTestActivity
import com.lodz.android.agiledevkt.modules.security.EncryptTestActivity
import com.lodz.android.agiledevkt.modules.selector.SelectorTestActivity
import com.lodz.android.agiledevkt.modules.setting.SettingTestActivity
import com.lodz.android.agiledevkt.modules.shapeableimg.ShapeableImgTestActivity
import com.lodz.android.agiledevkt.modules.snackbar.SnackbarTestActivity
import com.lodz.android.agiledevkt.modules.statusbar.StatusBarTestActivity
import com.lodz.android.agiledevkt.modules.str.StrTestActivity
import com.lodz.android.agiledevkt.modules.tabbar.TabBarTestActivity
import com.lodz.android.agiledevkt.modules.threadpool.ThreadPoolActivity
import com.lodz.android.agiledevkt.modules.toast.ToastTestActivity
import com.lodz.android.agiledevkt.modules.transition.TransitionActivity
import com.lodz.android.agiledevkt.modules.viewbinding.ViewBindingTestActivity
import com.lodz.android.agiledevkt.modules.viewpager.ViewPagerActivity
import com.lodz.android.agiledevkt.modules.watermark.WatermarkActivity
import com.lodz.android.agiledevkt.modules.webview.WebViewActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.group
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.anko.getStringCompat
import com.lodz.android.pandora.widget.base.compose.base.BaseContent
import com.lodz.android.pandora.widget.base.compose.base.BaseContentState
import com.lodz.android.pandora.widget.base.compose.base.Orientation
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBar
import com.lodz.android.pandora.widget.base.compose.titlebar.titleBarOptionCreate
import com.lodz.android.pandora.widget.index.compose.IndexBar
import com.lodz.android.pandora.widget.index.compose.IndexBarOptionCreate
import java.util.Random

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

    /** 索引文字 */
    var indexText by remember { mutableStateOf("") }

    /** 是否显示索引提示 */
    var isShowIndexTips by remember { mutableStateOf(false) }

    /** 主页列表 */
    val mainDataList = arrayListOf(
        MainBean("文件测试类", "W", FileTestActivity::class.java),
        MainBean("加密测试类", "J", EncryptTestActivity::class.java),
        MainBean("Bitmap图片测试类", "B", BitmapTestActivity::class.java),
        MainBean("Glide测试", "G", GlideActivity::class.java),
        MainBean("通知测试类", "T", NotificationActivity::class.java),
        MainBean("Toast测试类", "T", ToastTestActivity::class.java),
        MainBean("反射测试类", "F", ReflectActivity::class.java),
        MainBean("颜色透明度测试", "Y", ColorAlphaTestActivity::class.java),
        MainBean("线程池测试类", "X", ThreadPoolActivity::class.java),
        MainBean("身份证号码测试类", "S", IdcardTestActivity::class.java),
        MainBean("数字格式化测试类", "S", NumFormatTestActivity::class.java),
        MainBean("背景选择器测试类", "B", SelectorTestActivity::class.java),
        MainBean("设置测试类", "S", SettingTestActivity::class.java),
        MainBean("字符测试类", "Z", StrTestActivity::class.java),
        MainBean("信息展示测试类", "X", InfoTestActivity::class.java),
        MainBean("蓝牙测试类", "L", BluetoothTestActivity::class.java),
        MainBean("状态栏透明颜色测试类", "Z", StatusBarTestActivity::class.java),
        MainBean("侧滑栏测试类", "C", DrawerTestActivity::class.java),
        MainBean("Coordinator测试类", "C", CoordinatorTestActivity::class.java),
        MainBean("Snackbar测试类", "S", SnackbarTestActivity::class.java),
        MainBean("崩溃测试类", "B", CrashTestActivity::class.java),
        MainBean("定位测试", "D", LocationTestActivity::class.java),
        MainBean("Dialog测试", "D", DialogActivity::class.java),
        MainBean("DialogFragment测试类", "D", DialogFragmentActivity::class.java),
        MainBean("数组列表测试类", "S", ArrayTestActivity::class.java),
        MainBean("ACache缓存测试类", "A", ACacheTestActivity::class.java),
        MainBean("MVC模式测试类", "M", MvcDemoActivity::class.java),
        MainBean("共享元素动画", "G", TransitionActivity::class.java),
        MainBean("RV动画测试", "R", AnimRvActivity::class.java),
        MainBean("RV刷新/加载更多测试", "R", RefreshLoadMoreMainActivity::class.java),
        MainBean("RV带头/底部测试", "R", HeadFooterRvActivity::class.java),
        MainBean("RV拖拽测试", "R", DragRvActivity::class.java),
        MainBean("RV树结构测试类", "R", TreeRvActivity::class.java),
        MainBean("RvSnap测试类", "R", SnapRvActivity::class.java),
        MainBean("RV装饰器测试", "R", DecorationRvActivity::class.java),
        MainBean("RV侧滑菜单测试", "R", SwipeRvActivity::class.java),
        MainBean("RecyclerBinder测试类", "R", BinderRvActivity::class.java),
        MainBean("BottomSheets测试类", "B", BottomSheetsActivity::class.java),
        MainBean("文件选择测试类", "W", PicActivity::class.java),
        MainBean("手机文件测试类", "S", PhoneFileActivity::class.java),
        MainBean("加载框测试类", "J", ProgressDialogActivity::class.java),
        MainBean("Rx帮助类测试", "R", RxUtilsTestActivity::class.java),
        MainBean("RxObservable订阅测试", "R", RxObservableActivity::class.java),
        MainBean("RxFlowable订阅测试", "R", RxFlowableActivity::class.java),
        MainBean("RxSingle订阅测试", "R", RxSingleActivity::class.java),
        MainBean("RxMaybe订阅测试", "R", RxMaybeActivity::class.java),
        MainBean("RxCompletable订阅测试", "R", RxCompletableActivity::class.java),
        MainBean("底部菜单栏测试类", "D", MenuBarTestActivity::class.java),
        MainBean("顶部菜单栏测试类", "D", TabBarTestActivity::class.java),
        MainBean("采集测试页", "C", CollectActivity::class.java),
        MainBean("接口测试类", "J", ApiTestActivity::class.java),
        MainBean("日期测试类", "R", DateActivity::class.java),
        MainBean("协程测试类", "X", CoroutinesActivity::class.java),
        MainBean("单位转换测试类", "D", DimensionsActivity::class.java),
        MainBean("基础控件配置", "J", ConfigLayoutActivity::class.java),
        MainBean("注解测试类", "Z", AnnotationTestActivity::class.java),
        MainBean("WebView测试类", "W", WebViewActivity::class.java),
        MainBean("CardView测试类", "C", CardViewActivity::class.java),
        MainBean("MVVM模式测试类", "M", MvvmDemoActivity::class.java),
        MainBean("ViewPager2测试类", "V", ViewPagerActivity::class.java),
        MainBean("Fragment生命周期测试", "F", FragmentLifecycleActivity::class.java),
        MainBean("下载测试", "X", DownloadMarketActivity::class.java),
        MainBean("子控件透明布局测试类", "Z", HoleTestActivity::class.java),
        MainBean("MVP模式测试类", "M", MvpDemoActivity::class.java),
        MainBean("相机测试类", "X", CameraMainActivity::class.java),
        MainBean("自定义键盘测试类", "Z", KeyboardTestActivity::class.java),
        MainBean("ViewBinding测试类", "V", ViewBindingTestActivity::class.java),
        MainBean("Bytes字节转换测试类", "B", BytesTestActivity::class.java),
        MainBean("参数传递测试类", "C", ExtrasTestActivity::class.java),
        MainBean("ActivityResultContracts用例", "A", ResultContractsCaseActivity::class.java),
        MainBean("通讯录测试类", "T", ContactTestActivity::class.java),
        MainBean("Json测试类", "J", JsonTestActivity::class.java),
        MainBean("Shapeable图片测试类", "S", ShapeableImgTestActivity::class.java),
        MainBean("角标测试类", "J", BadgeTestActivity::class.java),
        MainBean("吸边控件展示类", "X", AdsorbViewActivity::class.java),
        MainBean("水印测试", "S", WatermarkActivity::class.java),
        MainBean("DataStore测试类", "D", DataStoreActivity::class.java)
    ).group(indexList).toList()

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

            StickyList(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(list) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                data = mainDataList,
                navController = nav
            )

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
                onIndexSelected = { position, text ->
                    isShowIndexTips = true
                    indexText = text
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
                        text = indexText,
                        color = Color.White,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun StickyList(modifier: Modifier, data: List<MainBean>, navController: NavController?) {
    val grouped = remember(data) { data.groupBy {it.getSortStr().first().toString()} }
    LazyColumn(modifier) {
        grouped.forEach { (title, items) ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .background(ColorsCompose.Color_f0f0f0)
                        .padding(start = 8.dp, end = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorsCompose.Color_00a0e9
                    )
                }
            }

            items(items) {
                ItemContainer(it, navController)
            }
        }
    }
}

@Composable
private fun ItemContainer(bean: MainBean, navController: NavController?) {
    // 颜色
    val colors = arrayOf(
        ColorsCompose.Color_00a0e9, ColorsCompose.Color_ea8380, ColorsCompose.Color_ea413c, ColorsCompose.Color_303f9f,
        ColorsCompose.Color_ff4081, ColorsCompose.Color_d28928, ColorsCompose.Color_464646
    )

    // Emoji表情
    val emojiUnicode = arrayOf(
        0x1F601, 0x1F602, 0x1F603, 0x1F604, 0x1F605, 0x1F606, 0x1F609, 0x1F60A, 0x1F60B, 0x1F60C, 0x1F60D, 0x1F60E, 0x1F60F,
        0x1F612, 0x1F613, 0x1F614, 0x1F616, 0x1F618, 0x1F61A, 0x1F61C, 0x1F61D, 0x1F61E, 0x1F620, 0x1F621, 0x1F622, 0x1F623,
        0x1F624, 0x1F625, 0x1F628, 0x1F629, 0x1F62A, 0x1F62B, 0x1F62D, 0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F634, 0x1F635,
        0x1F637, 0x1F638, 0x1F639, 0x1F63A, 0x1F63B, 0x1F63C, 0x1F63D, 0x1F63E, 0x1F63F
    )

    /** 随机数 */
    val random = Random()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController?.navigate(Route.detail(bean.getTitleName()))
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = String(Character.toChars(emojiUnicode[(random.nextInt(100) + 1) % emojiUnicode.size]))
                    .append("   ")
                    .append(bean.getTitleName()),
                color = colors[(random.nextInt(100) + 1) % colors.size],
                fontSize = 16.sp
            )
        }
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 5.dp, end = 5.dp)
                .background(ColorsCompose.Color_d9d9d9)
        )
    }
}