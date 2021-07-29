package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCoorStatusBarTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
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

    private val mBinding: ActivityCoorStatusBarTestBinding by bindingLayout(ActivityCoorStatusBarTestBinding::inflate)

    /** 列表适配器 */
    private lateinit var mAdapter: CoordinatorDataAdapter

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        mBinding.titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
        StatusBarUtil.setTransparentFullyForOffsetView(this@CoorStatusBarTestActivity, mBinding.toolbar)
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

        // 标题栏
        mBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 右侧按钮
        mBinding.rightBtn.setOnClickListener {
            finish()
        }

        // 浮动按钮
        mBinding.faBtn.setOnClickListener {
            SnackbarUtils.createShort(mBinding.recyclerView, getString(R.string.snackbar_sign_title))
                    .setTextColor(Color.WHITE)
                    .addLeftImage(R.drawable.ic_launcher, dp2px(5))
                    .getSnackbar()
                    .setActionTextColor(getColorCompat(R.color.color_ea5e5e))
                    .setAction(getString(R.string.snackbar_sign)) { view ->
                        toastShort(R.string.snackbar_sign)
                    }
                .show()
        }

        mBinding.appBarLayout.addOnOffsetChangedListener(object : OnAppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, delta: Double) {
                when (state) {
                    OnAppBarStateChangeListener.EXPANDED -> {// 完全展开
                        mBinding.rightBtn.visibility = View.VISIBLE
                        mBinding.rightBtn.alpha = 1f
                        mBinding.titleBarLayout.visibility = View.GONE
                    }
                    OnAppBarStateChangeListener.COLLAPSED -> {// 完全折叠
                        mBinding.rightBtn.visibility = View.GONE
                        mBinding.titleBarLayout.visibility = View.VISIBLE
                        mBinding.titleBarLayout.alpha = 1f
                    }
                    else -> { // 滑动中
                        mBinding.rightBtn.alpha = delta.toFloat()
                        mBinding.titleBarLayout.alpha = (1 - delta).toFloat()
                        mBinding.rightBtn.visibility = View.VISIBLE
                        mBinding.titleBarLayout.visibility = View.VISIBLE
                    }
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