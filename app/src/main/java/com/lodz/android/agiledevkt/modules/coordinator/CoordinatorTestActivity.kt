package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.statusbar.CoordinatorDataAdapter
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.componentkt.widget.contract.OnAppBarStateChangeListener
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.corekt.utils.toastShort
import java.util.*

/**
 * 带CoordinatorLayout的状态栏测试类
 * Created by zhouL on 2018/9/5.
 */
class CoordinatorTestActivity :AbsActivity(){

    companion object {
        fun start(context: Context, title: String) {
            val intent = Intent(context, CoordinatorTestActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_TITLE_NAME, title)
            context.startActivity(intent)
        }
    }

    /** AppBarLayout */
    @BindView(R.id.app_bar_layout)
    lateinit var mAppBarLayout: AppBarLayout
    /** Toolbar */
    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar
    /** 标题栏 */
    @BindView(R.id.title_bar_layout)
    lateinit var mTitleBarLayout: TitleBarLayout
    /** 右侧按钮 */
    @BindView(R.id.right_btn)
    lateinit var mRightBtn: ImageView
    /** 浮动按钮 */
    @BindView(R.id.fa_btn)
    lateinit var mFloatingActionBtn: FloatingActionButton
    /** 数据列表 */
    @BindView(R.id.recycler_view)
    lateinit var mRecyclerView: RecyclerView

    /** 列表适配器 */
    private lateinit var mAdapter: CoordinatorDataAdapter

    override fun getAbsLayoutId() = R.layout.activity_coordinator_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        mTitleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        initRecyclerView()
        StatusBarUtil.setTransparentFullyForOffsetView(getContext(), mToolbar)
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

        // 标题栏
        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mRightBtn.setOnClickListener {
            finish()
        }

        // 浮动按钮
        mFloatingActionBtn.setOnClickListener {
            SnackbarUtils.createShort(mRecyclerView, getString(R.string.snackbar_title))
                    .setTextColor(Color.WHITE)
                    .addLeftImage(R.drawable.ic_launcher, dp2px(5).toInt())
                    .getSnackbar()
                    .setActionTextColor(getColorCompat(R.color.color_ea5e5e))
                    .setAction(getString(R.string.snackbar_sign), { view ->
                        toastShort(R.string.snackbar_sign)
                    })
                    .show()
        }

        mAppBarLayout.addOnOffsetChangedListener(object : OnAppBarStateChangeListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, delta: Double) {
                if (state == OnAppBarStateChangeListener.EXPANDED){
                    // 完全展开
                    mRightBtn.visibility = View.VISIBLE
                    mRightBtn.alpha = 1f
                    mTitleBarLayout.visibility = View.GONE
                }else if (state == OnAppBarStateChangeListener.COLLAPSED){
                    // 完全折叠
                    mRightBtn.visibility = View.GONE
                    mTitleBarLayout.visibility = View.VISIBLE
                    mTitleBarLayout.alpha = 1f
                }else{
                    // 滑动中
                    mRightBtn.alpha = (1 - delta).toFloat()
                    mTitleBarLayout.alpha = delta.toFloat()
                    mRightBtn.visibility = View.VISIBLE
                    mTitleBarLayout.visibility = View.VISIBLE
                }

            }

        })
    }

    override fun initData() {
        super.initData()
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
}