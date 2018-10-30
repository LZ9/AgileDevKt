package com.lodz.android.agiledevkt.modules.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.anko.AnkoLayoutActivity
import com.lodz.android.agiledevkt.modules.bitmap.BitmapTestActivity
import com.lodz.android.agiledevkt.modules.bluetooth.BluetoothTestActivity
import com.lodz.android.agiledevkt.modules.color.ColorAlphaTestActivity
import com.lodz.android.agiledevkt.modules.coordinator.CoordinatorTestActivity
import com.lodz.android.agiledevkt.modules.crash.CrashTestActivity
import com.lodz.android.agiledevkt.modules.drawer.DrawerTestActivity
import com.lodz.android.agiledevkt.modules.file.FileTestActivity
import com.lodz.android.agiledevkt.modules.format.NumFormatTestActivity
import com.lodz.android.agiledevkt.modules.idcard.IdcardTestActivity
import com.lodz.android.agiledevkt.modules.image.GlideActivity
import com.lodz.android.agiledevkt.modules.info.InfoTestActivity
import com.lodz.android.agiledevkt.modules.location.LocationTestActivity
import com.lodz.android.agiledevkt.modules.notification.NotificationActivity
import com.lodz.android.agiledevkt.modules.reflect.ReflectActivity
import com.lodz.android.agiledevkt.modules.security.EncryptTestActivity
import com.lodz.android.agiledevkt.modules.selector.SelectorTestActivity
import com.lodz.android.agiledevkt.modules.setting.SettingTestActivity
import com.lodz.android.agiledevkt.modules.snackbar.SnackbarTestActivity
import com.lodz.android.agiledevkt.modules.statusbar.StatusBarTestActivity
import com.lodz.android.agiledevkt.modules.str.StrTestActivity
import com.lodz.android.agiledevkt.modules.threadpool.ThreadPoolActivity
import com.lodz.android.agiledevkt.modules.toast.ToastTestActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat

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

    /** 功能名称 */
    private val NAME_LIST = arrayListOf(
            "AnkoLayout测试类", "文件测试类", "加密测试类",
            "Bitmap图片测试类", "Glide测试", "通知测试类",
            "Toast测试类", "反射测试类", "颜色透明度测试",
            "线程池测试类", "身份证号码测试类", "数字格式化测试类",
            "背景选择器测试类", "设置测试类", "字符测试类",
            "信息展示测试类", "蓝牙测试类", "状态栏透明颜色测试类",
            "侧滑栏测试类", "Coordinator测试类", "Snackbar测试类",
            "崩溃测试类", "定位测试"
    )

    /** 功能的activity */
    private val CLASS_LIST = arrayListOf(
            AnkoLayoutActivity::class.java, FileTestActivity::class.java, EncryptTestActivity::class.java,
            BitmapTestActivity::class.java, GlideActivity::class.java, NotificationActivity::class.java,
            ToastTestActivity::class.java, ReflectActivity::class.java, ColorAlphaTestActivity::class.java,
            ThreadPoolActivity::class.java, IdcardTestActivity::class.java, NumFormatTestActivity::class.java,
            SelectorTestActivity::class.java, SettingTestActivity::class.java, StrTestActivity::class.java,
            InfoTestActivity::class.java, BluetoothTestActivity::class.java, StatusBarTestActivity::class.java,
            DrawerTestActivity::class.java, CoordinatorTestActivity::class.java, SnackbarTestActivity::class.java,
            CrashTestActivity::class.java, LocationTestActivity::class.java
    )

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)

    private lateinit var mAdapter: MainAdapter

    override fun getLayoutId() = R.layout.activity_main

    override fun findViews(savedInstanceState: Bundle?) {
        initTitleBar(getTitleBarLayout())
        initRecyclerView()
    }

    private fun initTitleBar(titleBarLayout: TitleBarLayout) {
        titleBarLayout.setTitleName(R.string.main_title)
        val refreshBtn = TextView(getContext())
        refreshBtn.text = getString(R.string.main_change_mood)
        refreshBtn.setPadding(dp2px(15).toInt(), 0, dp2px(15).toInt(), 0)
        refreshBtn.setTextColor(getColorCompat(R.color.white))
        refreshBtn.setOnClickListener {
            mAdapter.notifyDataSetChanged()
        }
        titleBarLayout.addExpandView(refreshBtn)
        titleBarLayout.needBackButton(false)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = MainAdapter(getContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    override fun onPressBack(): Boolean {
        App.get().exit()
        return true
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { holder, item, position ->
            val intent = Intent(getContext(), CLASS_LIST[position])
            intent.putExtra(EXTRA_TITLE_NAME, item)
            startActivity(intent)
        }
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(NAME_LIST)
        showStatusCompleted()
    }

}
