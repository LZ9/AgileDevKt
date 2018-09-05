package com.lodz.android.agiledevkt.modules.drawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.rx.subscribe.observer.ProgressObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.corekt.utils.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
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

    /** 内容 */
    @BindView(R.id.content_layout)
    lateinit var mContentLayout: ViewGroup
    /** 标题栏 */
    @BindView(R.id.title_bar_layout)
    lateinit var mTitleBarLayout: TitleBarLayout
    /** 透明度滚动条 */
    @BindView(R.id.alpha_sb)
    lateinit var mAlphaSeekBar: SeekBar
    /** 透明度值 */
    @BindView(R.id.alpha_value_tv)
    lateinit var mAlphaValueTv: TextView
    /** 结果 */
    @BindView(R.id.result)
    lateinit var mResultTv: TextView

    /** 侧滑栏 */
    @BindView(R.id.drawer_layout)
    lateinit var mDrawerLayout: DrawerLayout
    /** 用户头像 */
    @BindView(R.id.user_logo)
    lateinit var mUserLogoImg: ImageView
    /** 称号 */
    @BindView(R.id.title)
    lateinit var mTitleTv: TextView
    /** 搜索按钮 */
    @BindView(R.id.search_btn)
    lateinit var mSearchBtn: ImageView
    /** 刷新按钮 */
    @BindView(R.id.refresh_btn)
    lateinit var mRefreshBtn: ImageView
    /** 关注按钮 */
    @BindView(R.id.collect_btn)
    lateinit var mCollectBtn: ViewGroup
    /** 钱包按钮 */
    @BindView(R.id.wallet_btn)
    lateinit var mWalletBtn: ViewGroup
    /** 设置按钮 */
    @BindView(R.id.setting_btn)
    lateinit var mSettingBtn: ViewGroup

    override fun getAbsLayoutId() = R.layout.activity_drawer_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        mTitleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        mTitleTv.text = getString(R.string.drawer_title, TITLES.get(Random().nextInt(TITLES.size)))
        StatusBarUtil.setTransparentForDrawerLayout(getContext(), mDrawerLayout, mContentLayout, mAlphaSeekBar.progress / 100.0f)
    }

    override fun onPressBack(): Boolean {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        return super.onPressBack()
    }

    override fun setListeners() {
        super.setListeners()

        mTitleBarLayout.setOnBackBtnClickListener {
            if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.openDrawer(GravityCompat.START)
            }
        }

        mAlphaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                StatusBarUtil.setTransparentForDrawerLayout(getContext(), mDrawerLayout, mContentLayout, progress / 100.0f)
                updateAlphaValue()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 搜索
        mSearchBtn.setOnClickListener {
            mResultTv.text = getString(R.string.drawer_search)
            mDrawerLayout.closeDrawer(GravityCompat.START)
        }

        // 刷新
        mRefreshBtn.setOnClickListener {
            refreshTitle()
        }

        // 关注
        mCollectBtn.setOnClickListener {
            mResultTv.text = getString(R.string.drawer_collect)
            mDrawerLayout.closeDrawer(GravityCompat.START)
        }

        // 钱包
        mWalletBtn.setOnClickListener {
            mResultTv.text = getString(R.string.drawer_wallet)
            mDrawerLayout.closeDrawer(GravityCompat.START)
        }

        // 设置
        mSettingBtn.setOnClickListener {
            mResultTv.text = getString(R.string.drawer_setting)
            mDrawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun initData() {
        super.initData()
        ImageLoader.create(getContext())
                .load(R.drawable.bg_pokemon)
                .useCircle()
                .into(mUserLogoImg)
    }

    /** 刷新称号 */
    private fun refreshTitle() {
        val random = Random().nextInt(TITLES.size)
        Observable.just(TITLES.get(random))
                .map {title ->
                    Thread.sleep(1000)
                    title
                }
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : ProgressObserver<String>() {
                    override fun onPgNext(any: String) {
                        mTitleTv.text = getString(R.string.drawer_title, any)
                        toastShort(R.string.drawer_refresh_complete)
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        toastShort(R.string.drawer_refresh_fail)
                    }

                }.create(getContext(), R.string.drawer_refreshing, false))
    }

    /** 更新透明度 */
    private fun updateAlphaValue() {
        mAlphaValueTv.text = mAlphaSeekBar.progress.toString()
    }
}