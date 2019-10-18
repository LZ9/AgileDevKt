package com.lodz.android.agiledevkt.modules.bottomsheet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.widget.base.TitleBarLayout

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

    /** 标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)
    /** BottomSheets */
    private val mBottomSheetsBtn by bindView<MaterialButton>(R.id.bottom_sheets_btn)
    /** BottomSheetDialog */
    private val mDialogBtn by bindView<MaterialButton>(R.id.dialog_btn)
    /** BottomSheetDialogFragment */
    private val mDialogFragmentBtn by bindView<MaterialButton>(R.id.dialog_fragment_btn)
    /** 底部BottomSheet */
    private val mBottomSheet by bindView<NestedScrollView>(R.id.bottom_sheet)

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>

    override fun getAbsLayoutId(): Int = R.layout.activity_bottom_sheets

    override fun findViews(savedInstanceState: Bundle?) {
        mTitleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet)
    }

    override fun setListeners() {
        super.setListeners()
        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mBottomSheetsBtn.setOnClickListener {
            mBottomSheetBehavior.state = if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED else BottomSheetBehavior.STATE_EXPANDED
        }

        mDialogBtn.setOnClickListener {
            ImgBottomSheetDialog(getContext()).show()
        }

        mDialogFragmentBtn.setOnClickListener {
            TabBottomSheetDialogFragment().show(supportFragmentManager, "TabBottomSheetDialogFragment")
        }

        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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