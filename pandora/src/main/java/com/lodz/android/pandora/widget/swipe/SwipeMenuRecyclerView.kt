package com.lodz.android.pandora.widget.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * @author zhouL
 * @date 2022/10/8
 */
class SwipeMenuRecyclerView : RecyclerView{

    companion object {
        private const val INVALID_POSITION = -1
    }

    protected var mPdrScaleTouchSlop = 0
    protected var mPdrOldSwipedLayout: SwipeMenuLayout? = null
    protected var mPdrOldTouchedPosition = INVALID_POSITION

    private var mPdrDownX = 0f
    private var mPdrDownY = 0f

    private var isAllowSwipeDelete = false

    constructor(context: Context) : super(context) {
        mPdrScaleTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mPdrScaleTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPdrScaleTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    /** 关闭侧滑菜单 */
    fun smoothCloseMenu(){
        val oldSwipedLayout = mPdrOldSwipedLayout
        if (oldSwipedLayout != null && oldSwipedLayout.isMenuOpen()){
            oldSwipedLayout.smoothCloseMenu()
        }
    }

    /** 设置是否禁用侧滑[isEnabled] */
    fun setSwipeEnabled(isEnabled: Boolean) {
        isAllowSwipeDelete = isEnabled
    }

    /** 获取是否禁用侧滑 */
    fun isSwipeEnabled() = isAllowSwipeDelete

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        var isIntercepted = super.onInterceptTouchEvent(e)
        if (isAllowSwipeDelete){
            return isIntercepted
        }
        if (e == null){
            return isIntercepted
        }
        if (e.pointerCount > 1){
            return true
        }
        val action = e.action
        val x = e.x
        val y = e.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mPdrDownX = x
                mPdrDownY = y
                isIntercepted = false
                val view = findChildViewUnder(x, y) ?: return isIntercepted
                val touchingPosition = getChildAdapterPosition(view)
                val oldSwipedLayout = mPdrOldSwipedLayout
                if (touchingPosition != mPdrOldTouchedPosition && oldSwipedLayout != null && oldSwipedLayout.isMenuOpen()){
                    oldSwipedLayout.smoothCloseMenu()
                    isIntercepted = true
                }
                if (isIntercepted) {
                    mPdrOldSwipedLayout = null
                    mPdrOldTouchedPosition = INVALID_POSITION
                } else {
                    val vh = findViewHolderForAdapterPosition(touchingPosition)
                    if (vh != null){
                        val itemView = getSwipeMenuView(vh.itemView)
                        if (itemView is SwipeMenuLayout){
                            mPdrOldSwipedLayout = itemView
                            mPdrOldTouchedPosition = touchingPosition
                        }
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                isIntercepted = handleUnDown(x.toInt(), y.toInt(), isIntercepted)
                val oldSwipedLayout = mPdrOldSwipedLayout ?: return isIntercepted
                val disX = mPdrDownX - x
                // 向左滑，显示右侧菜单，或者关闭左侧菜单。
                val showRightCloseLeft = disX > 0 && (oldSwipedLayout.hasRightMenu() || oldSwipedLayout.isLeftCompleteOpen())
                // 向右滑，显示左侧菜单，或者关闭右侧菜单。
                val showLeftCloseRight = disX < 0 && (oldSwipedLayout.hasLeftMenu() || oldSwipedLayout.isRightCompleteOpen())
                parent.requestDisallowInterceptTouchEvent(showRightCloseLeft || showLeftCloseRight)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isIntercepted = handleUnDown(x.toInt(), y.toInt(), isIntercepted)
            }
        }
        return isIntercepted
    }

    private fun handleUnDown(x: Int, y: Int, defaultValue: Boolean): Boolean {
        val disX = mPdrDownX - x
        val disY = mPdrDownY - y

        // swipe
        if (abs(disX) > mPdrScaleTouchSlop && abs(disX) > abs(disY)){
            return false
        }
        // click
        if (abs(disY) < mPdrScaleTouchSlop && abs(disX) < mPdrScaleTouchSlop){
            return false
        }
        return defaultValue
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e == null){
            return super.onTouchEvent(e)
        }
        val action = e.action
        if (action == MotionEvent.ACTION_MOVE){
            val oldSwipedLayout = mPdrOldSwipedLayout
            if (oldSwipedLayout != null && oldSwipedLayout.isMenuOpen()){
                oldSwipedLayout.smoothCloseMenu()
            }
        }
        return super.onTouchEvent(e)
    }

    private fun getSwipeMenuView(itemView: View): View {
        if (itemView is SwipeMenuLayout){
            return itemView
        }
        val unvisited = ArrayList<View>()
        unvisited.add(itemView)
        while (unvisited.isNotEmpty()){
            val child = unvisited.removeAt(0)
            if (child !is ViewGroup){ // view
                continue
            }
            if (child is SwipeMenuLayout){
                return child
            }
            val childCount = child.childCount
            for (i in 0 until childCount) {
                unvisited.add(child.getChildAt(i))
            }

        }
        return itemView
    }
}