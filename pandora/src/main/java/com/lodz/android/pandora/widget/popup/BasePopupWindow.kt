package com.lodz.android.pandora.widget.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.LayoutRes

/**
 * PopupWindow基类
 * Created by zhouL on 2018/11/23.
 */
abstract class BasePopupWindow(context: Context) {
    /** 上下文 */
    private val mPdrContext: Context = context
    /** PopupWindow */
    private lateinit var mPdrPopupWindow: PopupWindow

    private fun createPopupWindow(context: Context): PopupWindow {
        val popView = if (getLayoutId() != 0) {
            LayoutInflater.from(context).inflate(getLayoutId(), null)
        } else {
            getLayoutView()
        }
        if (popView == null) {
            throw NullPointerException("please override getLayoutId() or getLayoutView() to create")
        }
        val popupWindow = PopupWindow(popView, getWidthPx(), getHeightPx(), true)
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = getElevationValue()
        }
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        return popupWindow
    }

    fun create() {
        mPdrPopupWindow = createPopupWindow(mPdrContext)
        startCreate()
        findViews(mPdrPopupWindow.contentView)
        setListeners()
        initData()
        endCreate()
    }

    /** 设置宽度，可重写该方法 */
    protected open fun getWidthPx(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    /** 设置高度，可重写该方法 */
    protected open fun getHeightPx(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    /** 设置阴影值，可重写该方法 */
    protected open fun getElevationValue(): Float = 12f

    protected open fun startCreate() {}

    @LayoutRes
    protected open fun getLayoutId(): Int = 0

    protected open fun getLayoutView(): View? = null

    protected open fun findViews(view: View) {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected fun getContext(): Context = mPdrContext

    fun getPopup(): PopupWindow = mPdrPopupWindow

}