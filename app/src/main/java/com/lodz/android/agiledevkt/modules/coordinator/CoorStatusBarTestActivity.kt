package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setupData
import kotlin.collections.ArrayList

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

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        mBinding.titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
        StatusBarUtil.setTransparentFullyForOffsetView(this@CoorStatusBarTestActivity, mBinding.toolbar)
    }

    private fun initRecyclerView() {
        mBinding.recyclerView
            .linear()
            .setupData<String>(R.layout.rv_item_coordinator) { holder, position ->
                val data = getItem(position)
                holder.withView<TextView>(R.id.data_tv).text = data
            }
            .setData(getListData())
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

    private fun getListData(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..50) {
            list.add(i.toString())
        }
        return list
    }
}