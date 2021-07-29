package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCoorTranslationBinding
import com.lodz.android.corekt.anko.dp2pxRF
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import com.lodz.android.pandora.widget.contract.OnAppBarStateChangeListener
import java.util.*

/**
 * 带CoordinatorLayout的位移测试类
 * Created by zhouL on 2018/9/11.
 */
class CoorTranslationActivity : AbsActivity() {

    companion object {
        /** 按钮右侧间距（单位dp） */
        private const val BTN_MARGIN_END_DP = 10

        fun start(context: Context) {
            val intent = Intent(context, CoorTranslationActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCoorTranslationBinding by bindingLayoutLazy(ActivityCoorTranslationBinding::inflate)

    private lateinit var mAdapter: CoordinatorDataAdapter

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

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = CoordinatorDataAdapter(getContext())
        mBinding.recyclerView.layoutManager = layoutManager
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.adapter = mAdapter
    }

    override fun setListeners() {
        super.setListeners()

        // 返回按钮
        mBinding.backBtn.setOnClickListener {
            finish()
        }

        // 订阅按钮
        mBinding.subscribeBtn.setOnClickListener {
            toastShort(R.string.coordinator_subscribe_tips)
        }

        mBinding.appBarLayout.addOnOffsetChangedListener(object : OnAppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, delta: Double) {
                if (mToolbarCenterY == 0f){
                    return
                }

                // 图片缩放和位移
                mBinding.headImg.scaleX = delta.toFloat()
                mBinding.headImg.scaleY = delta.toFloat()
                mBinding.headImg.translationY = (mHeadImgCenterY - mToolbarCenterY) * (delta.toFloat() - 1)

                // 用户名位移
                mBinding.userNameTv.translationY = (mUserNameCenterY - mToolbarCenterY) * (delta.toFloat() - 1)

                // 订阅按钮位移
                mBinding.subscribeBtn.translationY = (mSubscribeBtnCenterY - mToolbarCenterY) * (delta.toFloat() - 1)
                mBinding.subscribeBtn.translationX = (getScreenWidth() - mBinding.subscribeBtn.width / 2 - dp2pxRF(BTN_MARGIN_END_DP) - mSubscribeBtnCenterX) * (1 - delta.toFloat())
            }
        })
    }

    override fun initData() {
        super.initData()
        showHeadImg(mBinding.headImg)
        mBinding.userNameTv.setText(R.string.coordinator_user_name)
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
                .loadResId(R.drawable.bg_pokemon)
                .useCircle()
                .into(imageView)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            mToolbarCenterY = mBinding.toolbar.height / 2.0f
            mHeadImgCenterY = mBinding.headImg.top + mBinding.headImg.height / 2.0f
            mUserNameCenterY = mBinding.userNameTv.top + mBinding.userNameTv.height / 2.0f
            mSubscribeBtnCenterY = mBinding.subscribeBtn.top + mBinding.subscribeBtn.height / 2.0f
            mSubscribeBtnCenterX = mBinding.subscribeBtn.left + mBinding.subscribeBtn.width / 2.0f
        }
    }
}