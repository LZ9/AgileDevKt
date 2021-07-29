package com.lodz.android.agiledevkt.modules.drawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.view.GravityCompat
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDrawerTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.trello.rxlifecycle4.android.ActivityEvent
import io.reactivex.rxjava3.core.Observable
import java.util.*

/**
 * 侧滑栏测试类
 * Created by zhouL on 2018/9/3.
 */
class DrawerTestActivity : AbsActivity() {

    companion object {
        fun start(context: Context, title: String) {
            val intent = Intent(context, DrawerTestActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_TITLE_NAME, title)
            context.startActivity(intent)
        }
    }

    private val TITLES = arrayOf("无名的旅人", "路旁的落叶", "水面上的小草", "呢喃的歌声", "地上的月影", "奔跑的春风",
            "苍之风云", "摇曳的金星", "欢喜的慈雨", "蕴含的太阳", "敬畏的寂静", "无尽星空")

    private val mBinding: ActivityDrawerTestBinding by bindingLayout(ActivityDrawerTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        mBinding.titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        mBinding.drawerLeftView.titleTv.text = getString(R.string.drawer_title, TITLES[Random().nextInt(TITLES.size)])
        StatusBarUtil.setTransparentForDrawerLayout(
            this@DrawerTestActivity,
            mBinding.drawerLayout,
            mBinding.contentLayout,
            mBinding.alphaSb.progress / 100.0f
        )
    }

    override fun onPressBack(): Boolean {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        return super.onPressBack()
    }

    override fun setListeners() {
        super.setListeners()

        // 标题栏返回按钮
        mBinding.titleBarLayout.setOnBackBtnClickListener {
            if (!mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                mBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // 透明度滚动条
        mBinding.alphaSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                StatusBarUtil.setTransparentForDrawerLayout(
                    this@DrawerTestActivity,
                    mBinding.drawerLayout,
                    mBinding.contentLayout,
                    progress / 100.0f
                )
                updateAlphaValue()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 搜索
        mBinding.drawerLeftView.searchBtn.setOnClickListener {
            mBinding.resultTv.text = getString(R.string.drawer_search)
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 刷新
        mBinding.drawerLeftView.refreshBtn.setOnClickListener {
            refreshTitle()
        }

        // 关注
        mBinding.drawerLeftView.collectBtn.setOnClickListener {
            mBinding.resultTv.text = getString(R.string.drawer_collect)
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 钱包
        mBinding.drawerLeftView.walletBtn.setOnClickListener {
            mBinding.resultTv.text = getString(R.string.drawer_wallet)
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 设置
        mBinding.drawerLeftView.settingBtn.setOnClickListener {
            mBinding.resultTv.text = getString(R.string.drawer_setting)
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun initData() {
        super.initData()
        // 设置用户头像
        ImageLoader.create(getContext())
                .loadResId(R.drawable.bg_pokemon)
                .useCircle()
                .into(mBinding.drawerLeftView.userLogoImg)
    }

    /** 刷新称号 */
    private fun refreshTitle() {
        val random = Random().nextInt(TITLES.size)
        Observable.just(TITLES[random])
            .map { title ->
                Thread.sleep(1000)
                title
            }
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(
                ProgressObserver.action(
                    getContext(),
                    getString(R.string.drawer_refreshing),
                    false,
                    next = { title ->
                        mBinding.drawerLeftView.titleTv.text = getString(R.string.drawer_title, title)
                        toastShort(R.string.drawer_refresh_complete)
                    },
                    error = { e, isNetwork ->
                        toastShort(R.string.drawer_refresh_fail)
                    }
                )
            )
    }

    /** 更新透明度 */
    private fun updateAlphaValue() {
        mBinding.alphaValueTv.text = mBinding.alphaSb.progress.toString()
    }
}