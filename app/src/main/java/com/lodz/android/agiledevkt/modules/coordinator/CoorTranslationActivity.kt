package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.widget.contract.OnAppBarStateChangeListener
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.corekt.utils.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import java.util.*

/**
 * 带CoordinatorLayout的位移测试类
 * Created by zhouL on 2018/9/11.
 */
class CoorTranslationActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CoorTranslationActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 按钮右侧间距（单位dp） */
    private val BTN_MARGIN_END_DP = 10

    /** 返回按钮 */
    @BindView(R.id.back_btn)
    lateinit var mBackBtn: ImageView

    /** 头像 */
    @BindView(R.id.head_img)
    lateinit var mHeadImg: ImageView
    /** 用户名 */
    @BindView(R.id.user_name_tv)
    lateinit var mUserNameTv: TextView
    /** 订阅按钮 */
    @BindView(R.id.subscribe_btn)
    lateinit var mSubscribeBtn: TextView

    /** 数据列表 */
    @BindView(R.id.recycler_view)
    lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: CoordinatorDataAdapter

    /** AppBarLayout */
    @BindView(R.id.app_bar_layout)
    lateinit var mAppBarLayout: AppBarLayout
    /** Toolbar */
    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    /** 标题栏中心Y坐标 */
    private var mToolbarCenterY: Float = 0f
    /** 头像中心Y坐标 */
    private var mHeadImgCenterY: Float = 0f
    /** 用户名中心Y坐标 */
    private var mUserNameCenterY: Float = 0f
    /** 订阅按钮中心Y坐标 */
    private var mSubscribeBtnCenterY: Float = 0f
    /** 订阅按钮中心X坐标 */
    private var mSubscribeBtnCenterX: Float = 0f

    override fun getAbsLayoutId() = R.layout.activity_coor_translation

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mAdapter = CoordinatorDataAdapter(getContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    override fun setListeners() {
        super.setListeners()

        mBackBtn.setOnClickListener {
            finish()
        }

        mSubscribeBtn.setOnClickListener {
            toastShort(R.string.coordinator_subscribe_tips)
        }

        mAppBarLayout.addOnOffsetChangedListener(object : OnAppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, delta: Double) {
                if (mToolbarCenterY == 0f){
                    return
                }

                // 图片缩放和位移
                mHeadImg.scaleX = delta.toFloat()
                mHeadImg.scaleY = delta.toFloat()
                mHeadImg.translationY = (mHeadImgCenterY - mToolbarCenterY) * (delta.toFloat() - 1)

                // 用户名位移
                mUserNameTv.translationY = (mUserNameCenterY - mToolbarCenterY) * (delta.toFloat() - 1)

                // 订阅按钮位移
                mSubscribeBtn.translationY = (mSubscribeBtnCenterY - mToolbarCenterY) * (delta.toFloat() - 1)
                mSubscribeBtn.translationX = (getScreenWidth() - mSubscribeBtn.width / 2 - dp2px(BTN_MARGIN_END_DP) - mSubscribeBtnCenterX) * (1 - delta.toFloat())
            }
        })
    }

    override fun initData() {
        super.initData()
        showHeadImg(mHeadImg)
        mUserNameTv.setText(R.string.coordinator_user_name)
        initListData()
    }

    private fun initListData() {
        val list = ArrayList<String>()
        for (i in 1..50) {
            list.add(i.toString())
        }
        mAdapter.setData(list)
        mAdapter.notifyDataSetChanged()
    }

    /** 显示头像 */
    private fun showHeadImg(imageView: ImageView) {
        ImageLoader.create(this)
                .load(R.drawable.bg_pokemon)
                .useCircle()
                .into(imageView)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            mToolbarCenterY = mToolbar.height / 2.0f
            mHeadImgCenterY = mHeadImg.top + mHeadImg.height / 2.0f
            mUserNameCenterY = mUserNameTv.top + mUserNameTv.height / 2.0f
            mSubscribeBtnCenterY = mSubscribeBtn.top + mSubscribeBtn.height / 2.0f
            mSubscribeBtnCenterX = mSubscribeBtn.left + mSubscribeBtn.width / 2.0f
        }
    }
}