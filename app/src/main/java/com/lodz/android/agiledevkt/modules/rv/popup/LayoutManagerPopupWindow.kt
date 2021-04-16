package com.lodz.android.agiledevkt.modules.rv.popup

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.IntDef
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
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

    /** 线性布局 */
    private val mLinearBtn by getPopup().bindView<TextView>(R.id.linear_btn)
    /** 网格布局 */
    private val mGridBtn by getPopup().bindView<TextView>(R.id.grid_btn)
    /** 瀑布流 */
    private val mStaggeredBtn by getPopup().bindView<TextView>(R.id.staggered_btn)

    /** 点击 */
    private var mOnClickListener: ((popup: PopupWindow, type: Int) -> Unit)? = null

    override fun getLayoutId(): Int = R.layout.popup_layout_manager

    override fun findViews(view: View) {
        mLinearBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_LINEAR)
        }

        mGridBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_GRID)
        }

        mStaggeredBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_STAGGERED)
        }
    }

    /** 设置布局类型[type] */
    fun setLayoutManagerType(@LayoutManagerType type: Int) {
        mLinearBtn.setBackgroundResource(if (type == TYPE_LINEAR) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mLinearBtn.setTextColor(getContext().getColorCompat(if (type == TYPE_LINEAR) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mGridBtn.setBackgroundResource(if (type == TYPE_GRID) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mGridBtn.setTextColor(getContext().getColorCompat(if (type == TYPE_GRID) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mStaggeredBtn.setBackgroundResource(if (type == TYPE_STAGGERED) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mStaggeredBtn.setTextColor(getContext().getColorCompat(if (type == TYPE_STAGGERED) R.color.color_00a0e9 else R.color.color_9a9a9a))
    }


    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (popup: PopupWindow, type: Int) -> Unit) {
        mOnClickListener = listener
    }
}