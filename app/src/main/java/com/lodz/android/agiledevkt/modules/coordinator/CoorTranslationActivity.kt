package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.widget.contract.OnAppBarStateChangeListener
import com.lodz.android.corekt.anko.bindView
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
    private val mBackBtn by bindView<ImageView>(R.id.back_btn)

    /** 头像 */
    private val mHeadImg by bindView<ImageView>(R.id.head_img)
    /** 用户名 */
    private val mUserNameTv by bindView<TextView>(R.id.user_name_tv)
    /** 订阅按钮 */
    private val mSubscribeBtn by bindView<TextView>(R.id.subscribe_btn)

    /** 数据列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private lateinit var mAdapter: CoordinatorDataAdapter

    /** AppBarLayout */
    private val mAppBarLayout by bindView<AppBarLayout>(R.id.app_bar_layout)
    /** Toolbar */
    private val mToolbar by bindView<Toolbar>(R.id.toolbar)

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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
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