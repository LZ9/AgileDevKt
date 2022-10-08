package com.lodz.android.agiledevkt.modules.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.MainBean
import com.lodz.android.agiledevkt.databinding.ActivityMainBinding
import com.lodz.android.agiledevkt.modules.acache.ACacheTestActivity
import com.lodz.android.agiledevkt.modules.phonefile.PhoneFileActivity
import com.lodz.android.agiledevkt.modules.annotation.AnnotationTestActivity
import com.lodz.android.agiledevkt.modules.api.ApiTestActivity
import com.lodz.android.agiledevkt.modules.array.ArrayTestActivity
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
import com.lodz.android.agiledevkt.modules.pic.PicActivity
import com.lodz.android.agiledevkt.modules.progressdialog.ProgressDialogActivity
import com.lodz.android.agiledevkt.modules.reflect.ReflectActivity
import com.lodz.android.agiledevkt.modules.result.ResultContractsCaseActivity
import com.lodz.android.agiledevkt.modules.rv.anim.AnimRvActivity
import com.lodz.android.agiledevkt.modules.rv.binder.BinderRvActivity
import com.lodz.android.agiledevkt.modules.rv.decoration.DecorationRvActivity
import com.lodz.android.agiledevkt.modules.rv.drag.DragRvActivity
import com.lodz.android.agiledevkt.modules.rv.head.HeadFooterRvActivity
import com.lodz.android.agiledevkt.modules.rv.loadmore.RefreshLoadMoreActivity
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
import com.lodz.android.agiledevkt.modules.snackbar.SnackbarTestActivity
import com.lodz.android.agiledevkt.modules.statusbar.StatusBarTestActivity
import com.lodz.android.agiledevkt.modules.str.StrTestActivity
import com.lodz.android.agiledevkt.modules.threadpool.ThreadPoolActivity
import com.lodz.android.agiledevkt.modules.toast.ToastTestActivity
import com.lodz.android.agiledevkt.modules.transition.TransitionActivity
import com.lodz.android.agiledevkt.modules.viewbinding.ViewBindingTestActivity
import com.lodz.android.agiledevkt.modules.viewpager.ViewPagerActivity
import com.lodz.android.agiledevkt.modules.webview.WebViewActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.lodz.android.pandora.widget.index.IndexBar
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setup
import com.lodz.android.pandora.widget.rv.decoration.StickyItemDecoration

class MainActivity : BaseActivity() {

    companion object {
        /** 标题名称 */
        const val EXTRA_TITLE_NAME = "extra_title_name"

        /** 通过上下文[context]启动Activity */
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 索引标题 */
    private val INDEX_TITLE = arrayListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")

    private val MAIN_DATA_LIST = arrayListOf(
//        MainBean("AnkoLayout测试类", "A", AnkoLayoutActivity::class.java),
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
        MainBean("RV刷新/加载更多测试", "R", RefreshLoadMoreActivity::class.java),
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
//        MainBean("Koin注入测试类", "K", KoinTestActivity::class.java),
        MainBean("相机测试类", "X", CameraMainActivity::class.java),
        MainBean("自定义键盘测试类", "Z", KeyboardTestActivity::class.java),
        MainBean("ViewBinding测试类", "V", ViewBindingTestActivity::class.java),
        MainBean("Bytes字节转换测试类", "B", BytesTestActivity::class.java),
        MainBean("参数传递测试类", "C", ExtrasTestActivity::class.java),
        MainBean("ActivityResultContracts用例", "A", ResultContractsCaseActivity::class.java),
        MainBean("通讯录测试类", "T", ContactTestActivity::class.java),
        MainBean("Json测试类", "J", JsonTestActivity::class.java)
    )

    private val mBinding: ActivityMainBinding by bindingLayout(ActivityMainBinding::inflate)

    private lateinit var mAdapter: MainAdapter
    private lateinit var mList: List<MainBean>

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        initTitleBar(getTitleBarLayout())
        initRecyclerView()
        mBinding.indexBar.setHintTextView(mBinding.hintTv)
    }

    private fun initTitleBar(titleBarLayout: TitleBarLayout) {
        titleBarLayout.setTitleName(R.string.main_title)
        val refreshBtn = TextView(getContext())
        refreshBtn.text = getString(R.string.main_change_mood)
        refreshBtn.setPadding(dp2px(15), 0, dp2px(15), 0)
        refreshBtn.setTextColor(getColorCompat(R.color.white))
        refreshBtn.setOnClickListener {
            mAdapter.notifyDataSetChanged()
        }
        titleBarLayout.addExpandView(refreshBtn)
        titleBarLayout.needBackButton(false)
    }

    /** 初始化RV */
    private fun initRecyclerView() {
        mAdapter = mBinding.recyclerView.let {
            it.linear()
            it.addItemDecoration(getItemDecoration())
            it.setup(MainAdapter(getContext()))
        }.apply {
            setOnItemClickListener { holder, item, position ->
                val intent = Intent(getContext(), item.getCls())
                intent.putExtra(EXTRA_TITLE_NAME, item.getTitleName())
                startActivity(intent)
            }
        }
    }

    private fun getItemDecoration(): RecyclerView.ItemDecoration =
        StickyItemDecoration.create<String>(getContext())
            .setOnSectionCallback { position ->
                mList[position].getSortStr()
            }
            .setSectionTextSize(16f)
            .setSectionHeight(30)
            .setSectionTextTypeface(Typeface.DEFAULT_BOLD)
            .setSectionTextColorRes(R.color.color_00a0e9)
            .setSectionTextPaddingLeftDp(8)
            .setSectionBgColorRes(R.color.color_f0f0f0)


    override fun onPressBack(): Boolean {
        App.get().exit()
        return true
    }

    override fun setListeners() {
        super.setListeners()
        // 索引栏
        mBinding.indexBar.setOnIndexListener(object : IndexBar.OnIndexListener {
            override fun onStart(position: Int, indexText: String) {
                val layoutManager = mBinding.recyclerView.layoutManager
                if (layoutManager != null && layoutManager is LinearLayoutManager) {
                    layoutManager.scrollToPositionWithOffset(mList.getPositionByIndex(INDEX_TITLE, indexText), 0)
                }
            }

            override fun onEnd() {}
        })
    }

    override fun initData() {
        super.initData()
        mList = MAIN_DATA_LIST.group(INDEX_TITLE).toList()
        mBinding.indexBar.setIndexList(INDEX_TITLE)
        mAdapter.setData(mList.toMutableList())
        showStatusCompleted()
    }
}
