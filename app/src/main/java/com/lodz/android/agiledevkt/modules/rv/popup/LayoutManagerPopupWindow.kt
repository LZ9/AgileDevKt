package com.lodz.android.agiledevkt.modules.rv.popup

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.IntDef
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.PopupLayoutManagerBinding
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.popup.BasePopupWindow

/**
 * RV布局类型
 * Created by zhouL on 2018/11/26.
 */
class LayoutManagerPopupWindow(context: Context) : BasePopupWindow(context) {

    companion object {
        const val TYPE_LINEAR = 1
        const val TYPE_GRID = 2
        const val TYPE_STAGGERED = 3
    }

    @IntDef(TYPE_LINEAR, TYPE_GRID, TYPE_STAGGERED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class LayoutManagerType

    private val mBinding: PopupLayoutManagerBinding by getContext().bindingLayout(PopupLayoutManagerBinding::inflate)

    /** 点击 */
    private var mOnClickListener: ((popup: PopupWindow, type: Int) -> Unit)? = null

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(view: View) {
        // 线性布局
        mBinding.linearBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_LINEAR)
        }

        // 网格布局
        mBinding.gridBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_GRID)
        }

        // 瀑布流
        mBinding.staggeredBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_STAGGERED)
        }
    }

    /** 设置布局类型[type] */
    fun setLayoutManagerType(@LayoutManagerType type: Int) {
        mBinding.linearBtn.setBackgroundResource(if (type == TYPE_LINEAR) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.linearBtn.setTextColor(getContext().getColorCompat(if (type == TYPE_LINEAR) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.gridBtn.setBackgroundResource(if (type == TYPE_GRID) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.gridBtn.setTextColor(getContext().getColorCompat(if (type == TYPE_GRID) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.staggeredBtn.setBackgroundResource(if (type == TYPE_STAGGERED) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.staggeredBtn.setTextColor(getContext().getColorCompat(if (type == TYPE_STAGGERED) R.color.color_00a0e9 else R.color.color_9a9a9a))
    }


    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (popup: PopupWindow, type: Int) -> Unit) {
        mOnClickListener = listener
    }
}