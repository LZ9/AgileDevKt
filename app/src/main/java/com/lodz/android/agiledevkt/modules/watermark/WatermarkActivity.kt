package com.lodz.android.agiledevkt.modules.watermark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityWatermarkBinding
import com.lodz.android.agiledevkt.modules.dialog.BottomDialog
import com.lodz.android.agiledevkt.modules.dialog.CenterDialog
import com.lodz.android.agiledevkt.modules.dialog.LeftDialog
import com.lodz.android.agiledevkt.modules.dialog.NormalDialog
import com.lodz.android.agiledevkt.modules.dialog.RightDialog
import com.lodz.android.agiledevkt.modules.dialog.TopDialog
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.watermark.addWatermark

/**
 * 水印测试页
 * @author zhouL
 * @date 2023/6/30
 */
class WatermarkActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WatermarkActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityWatermarkBinding by bindingLayout(ActivityWatermarkBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 普通弹框
        mBinding.normalBtn.setOnClickListener {
            NormalDialog(getContext()).addWatermark().show()
        }

        // 中间弹框
        mBinding.centerBtn.setOnClickListener {
            CenterDialog(getContext()).addWatermark().show()
        }

        // 左侧弹框
        mBinding.leftBtn.setOnClickListener {
            LeftDialog(getContext()).addWatermark().show()
        }

        // 右侧弹框
        mBinding.rightBtn.setOnClickListener {
            RightDialog(getContext()).addWatermark().show()
        }

        // 底部弹框
        mBinding.bottomBtn.setOnClickListener {
            BottomDialog(getContext()).addWatermark().show()
        }

        // 顶部弹框
        mBinding.topBtn.setOnClickListener {
            TopDialog(getContext()).addWatermark().show()
        }
    }

    override fun initData() {
        super.initData()
        addWatermark()
        showStatusCompleted()
    }
}