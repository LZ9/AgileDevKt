package com.lodz.android.agiledevkt.modules.bottomsheet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lodz.android.agiledevkt.databinding.ActivityBottomSheetsBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy

/**
 * BottomSheets测试类
 * Created by zhouL on 2018/12/11.
 */
class BottomSheetsActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BottomSheetsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityBottomSheetsBinding by bindingLayoutLazy(ActivityBottomSheetsBinding::inflate)

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        mBinding.titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.scrollView)
    }

    override fun setListeners() {
        super.setListeners()
        // 标题栏返回按钮
        mBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mBinding.bottomSheetsBtn.setOnClickListener {
            mBottomSheetBehavior.state = if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED else BottomSheetBehavior.STATE_EXPANDED
        }

        mBinding.dialogBtn.setOnClickListener {
            ImgBottomSheetDialog(getContext()).show()
        }

        mBinding.dialogFragmentBtn.setOnClickListener {
            TabBottomSheetDialogFragment().show(supportFragmentManager, "TabBottomSheetDialogFragment")
        }

        mBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //这里是bottomSheet 状态的改变
                PrintLog.d("testtag", "newState : $newState")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
                PrintLog.i("testtag", "slideOffset : $slideOffset")
            }
        })
    }
}