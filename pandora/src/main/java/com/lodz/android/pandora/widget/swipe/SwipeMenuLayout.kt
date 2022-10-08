package com.lodz.android.pandora.widget.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.OverScroller
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.lodz.android.pandora.R
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin


/**
 * @author zhouL
 * @date 2022/7/26
 */
class SwipeMenuLayout : FrameLayout{
    companion object{
        const val DEFAULT_SCROLLER_DURATION = 200
    }

    private var mPdrLeftViewId = 0
    private var mPdrContentViewId = 0
    private var mPdrRightViewId = 0

    private var mPdrOpenPercent = 0.5f
    private var mPdrScrollerDuration = DEFAULT_SCROLLER_DURATION

    private var mPdrScaledTouchSlop = 0
    private var mPdrLastX = 0
    private var mPdrLastY = 0
    private var mPdrDownX = 0
    private var mPdrDownY = 0
    private var mPdrContentView: View? = null
    private var mPdrSwipeLeftHorizontal: SwipeLeftHorizontal? = null
    private var mPdrSwipeRightHorizontal: SwipeRightHorizontal? = null
    private var mPdrSwipeCurrentHorizontal: SwipeHorizontal? = null
    private var isShouldResetSwipe = false
    private var isDragging = false
    private var isSwipeEnable = true
    private lateinit var mPdrScroller: OverScroller
    private var mPdrVelocityTracker: VelocityTracker? = null
    private var mPdrScaledMinimumFlingVelocity = 0
    private var mPdrScaledMaximumFlingVelocity = 0

    constructor(context: Context) : super(context) {
        init(null)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(attrs)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        configLayout(attrs)
        val configuration = ViewConfiguration.get(context)
        mPdrScaledTouchSlop = configuration.scaledTouchSlop
        mPdrScaledMinimumFlingVelocity = configuration.scaledMinimumFlingVelocity
        mPdrScaledMaximumFlingVelocity = configuration.scaledMaximumFlingVelocity
        mPdrScroller = OverScroller(context)
    }

    private fun configLayout(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout)
        mPdrLeftViewId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_leftViewId, mPdrLeftViewId)
        mPdrContentViewId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_contentViewId, mPdrContentViewId)
        mPdrRightViewId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_rightViewId, mPdrRightViewId)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mPdrLeftViewId != 0 && mPdrSwipeLeftHorizontal == null) {
            val view = findViewById<View>(mPdrLeftViewId)
            mPdrSwipeLeftHorizontal = SwipeLeftHorizontal(view)
        }
        if (mPdrRightViewId != 0 && mPdrSwipeRightHorizontal == null) {
            val view = findViewById<View>(mPdrRightViewId)
            mPdrSwipeRightHorizontal = SwipeRightHorizontal(view)
        }
        if (mPdrContentViewId != 0 && mPdrContentView == null) {
            mPdrContentView = findViewById(mPdrContentViewId)
        } else {
            val errorView = TextView(context)
            errorView.isClickable = true
            errorView.gravity = Gravity.CENTER
            errorView.textSize = 16f
            errorView.text = "You may not have set the ContentView."
            mPdrContentView = errorView
            addView(mPdrContentView)
        }
    }

    /** Set whether open swipe. Default is true. */
    fun setSwipeEnable(swipeEnable: Boolean) {
        isSwipeEnable = swipeEnable
    }

    /** Open the swipe function of the Item */
    fun isSwipeEnable() = isSwipeEnable

    /** Set open percentage. */
    fun setOpenPercent(openPercent: Float) {
        mPdrOpenPercent = openPercent
    }

    /** Get open percentage */
    fun getOpenPercent() = mPdrOpenPercent

    /** The duration of the set. */
    fun setScrollerDuration(scrollerDuration: Int) {
        mPdrScrollerDuration = scrollerDuration
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev?.action ?: return super.onInterceptTouchEvent(ev)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.x.toInt()
                val y = ev.y.toInt()
                mPdrLastX = x
                mPdrDownX = x
                mPdrDownY = y
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                val disX = (ev.x - mPdrDownX).toInt()
                val disY = (ev.y - mPdrDownY).toInt()
                return abs(disX) > mPdrScaledTouchSlop && abs(disX) > abs(disY)
            }
            MotionEvent.ACTION_UP -> {
                val horizontal = mPdrSwipeCurrentHorizontal
                val isClick = horizontal != null && horizontal.isClickOnContentView(width, ev.x)
                if (isMenuOpen() && isClick) {
                    smoothCloseMenu()
                    return true
                }
                return false
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!mPdrScroller.isFinished) {
                    mPdrScroller.abortAnimation()
                }
                return false
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null){
            return super.onTouchEvent(event)
        }
        if (mPdrVelocityTracker == null){
            mPdrVelocityTracker = VelocityTracker.obtain()
        }
        mPdrVelocityTracker?.addMovement(event)
        var dx = 0
        var dy = 0
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPdrLastX = event.x.toInt()
                mPdrLastY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isSwipeEnable()) {
                    return super.onTouchEvent(event)
                }
                val disX = (mPdrLastX - event.x).toInt()
                val disY = (mPdrLastY - event.y).toInt()
                if (!isDragging && abs(disX) > mPdrScaledTouchSlop && abs(disX) > abs(disY)) {
                    isDragging = true
                }
                if (!isDragging) {
                    return super.onTouchEvent(event)
                }
                if (mPdrSwipeCurrentHorizontal == null || isShouldResetSwipe) {
                    mPdrSwipeCurrentHorizontal = if (disX < 0) {
                        if (mPdrSwipeLeftHorizontal != null) mPdrSwipeLeftHorizontal else mPdrSwipeRightHorizontal
                    } else {
                        if (mPdrSwipeRightHorizontal != null) mPdrSwipeRightHorizontal else mPdrSwipeLeftHorizontal
                    }
                }
                scrollBy(disX ,0)
                mPdrLastX = event.x.toInt()
                mPdrLastY = event.y.toInt()
                isShouldResetSwipe = false
            }
            MotionEvent.ACTION_UP -> {
                dx = (mPdrDownX - event.x).toInt()
                dy = (mPdrDownY - event.y).toInt()
                isDragging = false
                mPdrVelocityTracker?.computeCurrentVelocity(1000, mPdrScaledMaximumFlingVelocity.toFloat())
                val velocityX = mPdrVelocityTracker?.xVelocity?.toInt() ?: 0
                val velocity = abs(velocityX)
                if (velocity > mPdrScaledMinimumFlingVelocity) {
                    if (mPdrSwipeCurrentHorizontal != null) {
                        val duration = getSwipeDuration(event, velocity)
                        if (mPdrSwipeCurrentHorizontal is SwipeRightHorizontal) {
                            if (velocityX < 0) smoothOpenMenu(duration) else smoothCloseMenu(duration)
                        } else {
                            if (velocityX > 0) smoothOpenMenu(duration) else smoothCloseMenu(duration)
                        }
                        ViewCompat.postInvalidateOnAnimation(this)
                    }
                } else {
                    judgeOpenClose(dx, dy)
                }
                mPdrVelocityTracker?.clear()
                mPdrVelocityTracker?.recycle()
                mPdrVelocityTracker = null
                if (abs(mPdrDownX - event.x) > mPdrScaledTouchSlop
                    || abs(mPdrDownY - event.y) > mPdrScaledTouchSlop
                    || isLeftMenuOpen()
                    || isRightMenuOpen()) {
                    event.action = MotionEvent.ACTION_CANCEL
                    super.onTouchEvent(event)
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                if (!mPdrScroller.isFinished) {
                    mPdrScroller.abortAnimation()
                } else {
                    dx = (mPdrDownX - event.x).toInt()
                    dy = (mPdrDownY - event.y).toInt()
                    judgeOpenClose(dx, dy)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /** compute finish duration. */
    private fun getSwipeDuration(ev: MotionEvent, velocity: Int): Int {
        val sx = scrollX
        val dx = (ev.x - sx).toInt()
        val width = mPdrSwipeCurrentHorizontal?.getMenuWidth() ?: 0
        val halfWidth = width / 2
        val distanceRatio = min(1f, 1.0f * abs(dx) / width).toDouble()
        val distance = halfWidth + halfWidth * distanceInfluenceForSnapDuration(distanceRatio)
        val duration: Int = if (velocity > 0) {
            4 * (1000f * abs(distance / velocity).toFloat()).roundToInt()
        } else {
            (abs(dx) / width + 1) * 100
        }
        return min(duration, mPdrScrollerDuration)
    }

    private fun distanceInfluenceForSnapDuration(d: Double): Double {
        var result = d
        result -= 0.5f // center the values about 0.
        result *= 0.3f * Math.PI / 2.0f
        return sin(result)
    }

    private fun judgeOpenClose(dx: Int, dy: Int) {
        val horizontal = mPdrSwipeCurrentHorizontal ?: return
        if (abs(scrollX) >= (horizontal.getMenuWidth() * mPdrOpenPercent)) {// auto open
            if (abs(dx) > mPdrScaledTouchSlop || abs(dy) > mPdrScaledTouchSlop) {// swipe up
                if (isMenuOpenNotEqual()) smoothCloseMenu() else smoothOpenMenu()
            } else {// normal up
                if (isMenuOpen()) smoothCloseMenu() else smoothOpenMenu()
            }
        } else {// auto closeMenu
            smoothCloseMenu()
        }
    }

    //private void judgeOpenClose(int dx, int dy) {
    //        if (mSwipeCurrentHorizontal != null) {
    //            if (Math.abs(getScrollX()) >= (mSwipeCurrentHorizontal.getMenuView().getWidth() * mOpenPercent)) { // auto open
    //                if (Math.abs(dx) > mScaledTouchSlop || Math.abs(dy) > mScaledTouchSlop) { // swipe up
    //                    if (isMenuOpenNotEqual()) smoothCloseMenu();
    //                    else smoothOpenMenu();
    //                } else { // normal up
    //                    if (isMenuOpen()) smoothCloseMenu();
    //                    else smoothOpenMenu();
    //                }
    //            } else { // auto closeMenu
    //                smoothCloseMenu();
    //            }
    //        }
    //    }

    override fun scrollTo(x: Int, y: Int) {
        if (mPdrSwipeCurrentHorizontal == null) {
            super.scrollTo(x, y)
            return
        }
        val checker = mPdrSwipeCurrentHorizontal?.checkXY(x, y)
        if (checker == null) {
            super.scrollTo(x, y)
            return
        }
        isShouldResetSwipe = checker.shouldResetSwipe
        if (checker.x != scrollX) {
            super.scrollTo(checker.x, checker.y)
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mPdrScroller.computeScrollOffset() && mPdrSwipeCurrentHorizontal != null) {
            if (mPdrSwipeCurrentHorizontal is SwipeRightHorizontal) {
                scrollTo(abs(mPdrScroller.currX), 0)
                invalidate()
            } else {
                scrollTo(-abs(mPdrScroller.currX), 0)
                invalidate()
            }
        }
    }

    fun hasLeftMenu(): Boolean = mPdrSwipeLeftHorizontal?.canSwipe() ?: false

    fun hasRightMenu(): Boolean = mPdrSwipeRightHorizontal?.canSwipe() ?: false

    fun isMenuOpen(): Boolean = isLeftMenuOpen() || isRightMenuOpen()

    fun isLeftMenuOpen(): Boolean = mPdrSwipeLeftHorizontal?.isMenuOpen(scrollX) ?: false

    fun isRightMenuOpen(): Boolean = mPdrSwipeRightHorizontal?.isMenuOpen(scrollX) ?: false

    fun isCompleteOpen(): Boolean = isLeftCompleteOpen() || isRightCompleteOpen()

    fun isLeftCompleteOpen(): Boolean {
        val left = mPdrSwipeLeftHorizontal
        return left != null && !left.isCompleteClose(scrollX)
    }

    fun isRightCompleteOpen(): Boolean {
        val right = mPdrSwipeRightHorizontal
        return right != null && !right.isCompleteClose(scrollX)
    }

    fun isMenuOpenNotEqual(): Boolean = isLeftMenuOpenNotEqual() || isRightMenuOpenNotEqual()

    fun isLeftMenuOpenNotEqual(): Boolean = mPdrSwipeLeftHorizontal?.isMenuOpenNotEqual(scrollX) ?: false

    fun isRightMenuOpenNotEqual(): Boolean = mPdrSwipeRightHorizontal?.isMenuOpenNotEqual(scrollX) ?: false

    fun smoothOpenMenu() = smoothOpenMenu(mPdrScrollerDuration)

    private fun smoothOpenMenu(duration: Int) {
        val horizontal = mPdrSwipeCurrentHorizontal
        if (horizontal != null) {
            horizontal.autoOpenMenu(mPdrScroller, scrollX, duration)
            invalidate()
        }
    }

    fun smoothOpenLeftMenu() = smoothOpenLeftMenu(mPdrScrollerDuration)

    fun smoothOpenLeftMenu(duration: Int) {
        if (mPdrSwipeLeftHorizontal != null) {
            mPdrSwipeCurrentHorizontal = mPdrSwipeLeftHorizontal
            smoothOpenMenu(duration)
        }
    }

    fun smoothOpenRightMenu() = smoothOpenRightMenu(mPdrScrollerDuration)

    fun smoothOpenRightMenu(duration: Int) {
        if (mPdrSwipeRightHorizontal != null) {
            mPdrSwipeCurrentHorizontal = mPdrSwipeRightHorizontal
            smoothOpenMenu(duration)
        }
    }

    fun smoothCloseLeftMenu() {
        if (mPdrSwipeLeftHorizontal != null) {
            mPdrSwipeCurrentHorizontal = mPdrSwipeLeftHorizontal
            smoothCloseMenu()
        }
    }

    fun smoothCloseRightMenu() {
        if (mPdrSwipeRightHorizontal != null) {
            mPdrSwipeCurrentHorizontal = mPdrSwipeRightHorizontal
            smoothCloseMenu()
        }
    }

    fun smoothCloseMenu() = smoothCloseMenu(mPdrScrollerDuration)

    fun smoothCloseMenu(duration: Int) {
        val horizontal = mPdrSwipeCurrentHorizontal
        if (horizontal != null) {
            horizontal.autoCloseMenu(mPdrScroller, scrollX, duration)
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isInEditMode){
            return
        }
        var contentViewHeight = 0
        val contentView = mPdrContentView
        if (contentView != null) {
            measureChildWithMargins(contentView, widthMeasureSpec, 0, heightMeasureSpec, 0)
            contentViewHeight = contentView.measuredHeight
        }

        val swipeLeftHorizontal = mPdrSwipeLeftHorizontal
        if (swipeLeftHorizontal != null){
            val leftMenu = swipeLeftHorizontal.getMenuView()
            val menuViewHeight = if (contentViewHeight == 0) leftMenu.measuredHeightAndState else contentViewHeight
            val menuWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST)
            val menuHeightSpec = MeasureSpec.makeMeasureSpec(menuViewHeight, MeasureSpec.EXACTLY)
            leftMenu.measure(menuWidthSpec, menuHeightSpec)
        }

        val swipeRightHorizontal = mPdrSwipeRightHorizontal
        if (swipeRightHorizontal != null){
            val rightMenu = swipeRightHorizontal.getMenuView()
            val menuViewHeight = if (contentViewHeight == 0) rightMenu.measuredHeightAndState else contentViewHeight
            val menuWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST)
            val menuHeightSpec = MeasureSpec.makeMeasureSpec(menuViewHeight, MeasureSpec.EXACTLY)
            rightMenu.measure(menuWidthSpec, menuHeightSpec)
        }

        if (contentViewHeight > 0){
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), contentViewHeight)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (isInEditMode){
            return
        }
        var contentViewHeight = 0
        val contentView = mPdrContentView
        if (contentView != null){
            val contentViewWidth = contentView.measuredWidthAndState
            contentViewHeight = contentView.measuredHeightAndState
            val lp = contentView.layoutParams as LayoutParams
            val start = paddingLeft
            val t = paddingTop + lp.topMargin
            contentView.layout(start, t, start + contentViewWidth, t + contentViewHeight)
        }

        val swipeLeftHorizontal = mPdrSwipeLeftHorizontal
        if (swipeLeftHorizontal != null) {
            val leftMenu = swipeLeftHorizontal.getMenuView()
            val menuViewWidth = leftMenu.measuredWidthAndState
            val menuViewHeight = leftMenu.measuredHeightAndState
            val lp = leftMenu.layoutParams as LayoutParams
            val t = paddingTop + lp.topMargin
            leftMenu.layout(-menuViewWidth, t, 0, t + menuViewHeight)
        }

        val swipeRightHorizontal = mPdrSwipeRightHorizontal
        if (swipeRightHorizontal != null) {
            val rightMenu = swipeRightHorizontal.getMenuView()
            val menuViewWidth = rightMenu.measuredWidthAndState
            val menuViewHeight = rightMenu.measuredHeightAndState
            val lp = rightMenu.layoutParams as LayoutParams
            val t = paddingTop + lp.topMargin
            val parentViewWidth = measuredWidthAndState
            rightMenu.layout(parentViewWidth, t, parentViewWidth + menuViewWidth, t + menuViewHeight)
        }
    }
}