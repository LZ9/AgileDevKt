package com.lodz.android.agiledevkt.modules.rv.popup

import android.content.Context
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.widget.popup.BasePopupWindow

/**
 * RV横纵方向PopupWindow
 * Created by zhouL on 2018/11/27.
 */
class OrientationPopupWindow(context: Context) : BasePopupWindow(context) {

    /** 纵向按钮 */
    private val mVerticalBtn by getPopup().bindView<TextView>(R.id.vertical_btn)
    /** 横向按钮 */
    private val mHorizontalBtn by getPopup().bindView<TextView>(R.id.horizontal_btn)

    /** 点击 */
    private var mOnClickListener: ((popup: PopupWindow, orientation: Int) -> Unit)? = null

    override fun getLayoutId(): Int = R.layout.popup_orientation

    override fun setListeners() {
        super.setListeners()
        mVerticalBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), RecyclerView.VERTICAL)
        }

        mHorizontalBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), RecyclerView.HORIZONTAL)
        }
    }

    /** 设置RV横纵类型[type] */
    fun setOrientationType(type: Int) {
        mVerticalBtn.setBackgroundResource(if (type == RecyclerView.VERTICAL) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mVerticalBtn.setTextColor(getContext().getColorCompat(if (type == RecyclerView.VERTICAL) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mHorizontalBtn.setBackgroundResource(if (type == RecyclerView.HORIZONTAL) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mHorizontalBtn.setTextColor(getContext().getColorCompat(if (type == RecyclerView.HORIZONTAL) R.color.color_00a0e9 else R.color.color_9a9a9a))
    }

    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (popup: PopupWindow, orientation: Int) -> Unit) {
        mOnClickListener = listener
    }
}