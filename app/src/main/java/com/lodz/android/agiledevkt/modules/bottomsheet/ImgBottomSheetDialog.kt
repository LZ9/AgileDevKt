package com.lodz.android.agiledevkt.modules.bottomsheet

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.lodz.android.pandora.widget.bottomsheets.dialog.BaseBottomSheetDialog

/**
 * 图片BottomSheetDialog
 * Created by zhouL on 2018/12/11.
 */
class ImgBottomSheetDialog(context: Context) : BaseBottomSheetDialog(context) {

    /** 是否用户关闭 */
    private var isUserDismiss = false

    private var mBehavior: BottomSheetBehavior<*>? = null

    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)

    override fun getLayoutId(): Int = R.layout.dialog_img_bottom_sheet

    override fun setListeners() {
        super.setListeners()
        mTitleBarLayout.setOnBackBtnClickListener {
            isUserDismiss = true
            mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onBehaviorInit(behavior: BottomSheetBehavior<*>) {
        mBehavior = behavior

        behavior.peekHeight = context.dp2px(200)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (!isUserDismiss) {
                    setDim(if (newState == BottomSheetBehavior.STATE_EXPANDED) 0f else 0.6f)
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    cancel()
                }
                mTitleBarLayout.needBackButton(newState == BottomSheetBehavior.STATE_EXPANDED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

}