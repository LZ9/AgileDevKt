package com.lodz.android.agiledevkt.modules.rv.popup

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.IntDef
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.widget.popup.BasePopupWindow
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

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
    private var mOnClickListener: ((PopupWindow, Int) -> Unit)? = null

    override fun getLayoutId(): Int = R.layout.popup_layout_manager

    override fun findViews(view: View) {
        mLinearBtn.setOnClickListener {
            if (mOnClickListener != null) {
                mOnClickListener!!.invoke(getPopup(), TYPE_LINEAR)
            }
        }

        mGridBtn.setOnClickListener {
            if (mOnClickListener != null) {
                mOnClickListener!!.invoke(getPopup(), TYPE_GRID)
            }
        }

        mStaggeredBtn.setOnClickListener {
            if (mOnClickListener != null) {
                mOnClickListener!!.invoke(getPopup(), TYPE_STAGGERED)
            }
        }
    }

    /** 设置布局类型[type] */
    fun set(@LayoutManagerType type: Int) {
        mLinearBtn.backgroundResource = if (type == TYPE_LINEAR) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mLinearBtn.textColor = getContext().getColorCompat(if (type == TYPE_LINEAR) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mGridBtn.backgroundResource = if (type == TYPE_GRID) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mGridBtn.textColor = getContext().getColorCompat(if (type == TYPE_GRID) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mStaggeredBtn.backgroundResource = if (type == TYPE_STAGGERED) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mStaggeredBtn.textColor = getContext().getColorCompat(if (type == TYPE_STAGGERED) R.color.color_00a0e9 else R.color.color_9a9a9a)
    }


    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (PopupWindow, Int) -> Unit) {
        mOnClickListener = listener
    }
}