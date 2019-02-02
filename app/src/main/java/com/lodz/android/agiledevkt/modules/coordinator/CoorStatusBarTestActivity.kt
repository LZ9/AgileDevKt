package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.lodz.android.pandora.widget.contract.OnAppBarStateChangeListener
import java.util.*

/**
 * 带CoordinatorLayout的状态栏测试类
 * Created by zhouL on 2018/9/5.
 */
class CoorStatusBarTestActivity : AbsActivity() {

    companion object {
        fun start(context: Context, title: String) {
            val intent = Intent(context, CoorStatusBarTestActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_TITLE_NAME, title)
            context.startActivity(intent)
        }
    }

    /** AppBarLayout */
    private val mAppBarLayout by bindView<AppBarLayout>(R.id.app_bar_layout)
    /** Toolbar */
    private val mToolbar by bindView<Toolbar>(R.id.toolbar)
    /** 标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)
    /** 右侧按钮 */
    private val mRightBtn by bindView<ImageView>(R.id.right_btn)
    /** 浮动按钮 */
    private val mFloatingActionBtn by bindView<FloatingActionButton>(R.id.fa_btn)
    /** 数据列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)

    /** 列表适配器 */
    private lateinit var mAdapter: CoordinatorDataAdapter

    override fun getAbsLayoutId() = R.layout.activity_coor_status_bar_test

    override fun findViews(savedInstanceState: Bundle?) {
        mTitleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        initRecyclerView()
        StatusBarUtil.setTransparentFullyForOffsetView(this@CoorStatusBarTestActivity, mToolbar)
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

        // 标题栏
        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mRightBtn.setOnClickListener {
            finish()
        }

        // 浮动按钮
        mFloatingActionBtn.setOnClickListener {
            SnackbarUtils.createShort(mRecyclerView, getString(R.string.snackbar_sign_title))
                    .setTextColor(Color.WHITE)
                    .addLeftImage(R.drawable.ic_launcher, dp2px(5).toInt())
                    .getSnackbar()
                    .setActionTextColor(getColorCompat(R.color.color_ea5e5e))
                    .setAction(getString(R.string.snackbar_sign), { view ->
                        toastShort(R.string.snackbar_sign)
                    })
                    .show()
        }

        mAppBarLayout.addOnOffsetChangedListener(object : OnAppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, delta: Double) {
                if (state == OnAppBarStateChangeListener.EXPANDED) {
                    // 完全展开
                    mRightBtn.visibility = View.VISIBLE
                    mRightBtn.alpha = 1f
                    mTitleBarLayout.visibility = View.GONE
                } else if (state == OnAppBarStateChangeListener.COLLAPSED) {
                    // 完全折叠
                    mRightBtn.visibility = View.GONE
                    mTitleBarLayout.visibility = View.VISIBLE
                    mTitleBarLayout.alpha = 1f
                } else {
                    // 滑动中
                    mRightBtn.alpha = delta.toFloat()
                    mTitleBarLayout.alpha = (1 - delta).toFloat()
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