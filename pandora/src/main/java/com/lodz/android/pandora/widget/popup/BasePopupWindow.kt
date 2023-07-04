package com.lodz.android.pandora.widget.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
    private var mPdrPopupWindow: PopupWindow

    init {
        mPdrPopupWindow = createPopupWindow(mPdrContext)
    }

    private fun createPopupWindow(context: Context): PopupWindow {
        val layoutId = getLayoutId()
        val popView =  if (layoutId != 0) LayoutInflater.from(context).inflate(layoutId, null) else null
        val popupWindow = if (popView != null) {
            PopupWindow(wrapperLayout(popView), getWidthPx(), getHeightPx(), true)
        } else {
            PopupWindow(getWidthPx(), getHeightPx())
        }
        popupWindow.isFocusable = true
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.elevation = getElevationValue()
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        return popupWindow
    }

    fun create(): PopupWindow {
        if (mPdrPopupWindow.contentView == null){
            mPdrPopupWindow.contentView = wrapperLayout(getViewBindingLayout())
        }
        if (mPdrPopupWindow.contentView == null) {
            throw NullPointerException("please override getLayoutId() or getViewBindingLayout() to create")
        }
        startCreate()
        findViews(mPdrPopupWindow.contentView)
        setListeners()
        initData()
        endCreate()
        return mPdrPopupWindow
    }

    /** 包装布局 */
    private fun wrapperLayout(view: View?): FrameLayout? {
        if (view == null) {
            return null
        }
        val layout = FrameLayout(mPdrContext)
        layout.addView(view, getWidthPx(), getHeightPx())
        return layout
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

    protected open fun getViewBindingLayout(): View? = null

    protected open fun findViews(view: View) {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected fun getContext(): Context = mPdrContext

    fun getPopup(): PopupWindow = mPdrPopupWindow

}