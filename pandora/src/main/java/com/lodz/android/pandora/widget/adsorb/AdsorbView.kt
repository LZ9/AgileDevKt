package com.lodz.android.pandora.widget.adsorb

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.*
import com.lodz.android.pandora.R
import kotlin.math.abs

/**
 * 吸边控件
 * @author zhouL
 * @date 2022/10/18
 */
abstract class AdsorbView : FrameLayout, View.OnTouchListener {

    companion object {
        /** 垂直上下 */
        const val VERTICAL = 0
        /** 水平左右 */
        const val HORIZONTAL = 1
    }

    /** 吸边类型 */
    private var mAdsorbType = HORIZONTAL
    /** 是否允许拖拽 */
    private var isCanDrag = true
    /** 动画时长 */
    private var mAnimDuration = 300L

    /** 父控件宽度 */
    private var mParentWidth = 0
    /** 父控件高度 */
    private var mParentHeigth = 0

    private var mParentPaddingEnd = 0
    private var mParentPaddingStart = 0
    private var mParentPaddingTop = 0
    private var mParentPaddingBottom = 0

    /** 当前控件宽度 */
    private var mViewWidth = 0
    /** 当前控件高度 */
    private var mViewHeigth = 0

    private var mListener: OnClickListener? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        configLayout(attrs)
        addView(getContentView())
        setOnTouchListener(this)
        post {
            val view = parent as View
            mParentWidth = view.right - view.left
            mParentHeigth = view.bottom - view.top
            mParentPaddingTop = view.paddingTop
            mParentPaddingBottom = view.paddingBottom
            mParentPaddingStart = view.paddingStart
            mParentPaddingEnd = view.paddingEnd

            mViewWidth = this.right - this.left
            mViewHeigth = this.bottom - this.top
        }
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdsorbView)
        }
        setCanDrag(typedArray?.getBoolean(R.styleable.AdsorbView_isCanDrag, true) ?: true)
        setAdsorbType(typedArray?.getInt(R.styleable.AdsorbView_adsorbType, HORIZONTAL) ?: HORIZONTAL)
        setAnimDuration(typedArray?.getInt(R.styleable.AdsorbView_animDuration, 300)?.toLong() ?: 300L)
        typedArray?.recycle()
    }

    abstract fun getContentView(): View

    /** 设置吸边类型[type] */
    fun setAdsorbType(type: Int) {
        mAdsorbType = type
    }

    /** 获取吸边类型 */
    fun getAdsorbType(): Int = mAdsorbType

    /** 设置动画时长[duration] */
    fun setAnimDuration(duration: Long) {
        if (duration > 0) {
            mAnimDuration = duration
        }
    }

    /** 获取动画时长 */
    fun getAnimDuration(): Long = mAnimDuration

    /** 设置是否允许拖拽[isCanDrag] */
    fun setCanDrag(isCanDrag: Boolean) {
        this.isCanDrag = isCanDrag
    }

    /** 是否允许拖拽 */
    fun isCanDrag(): Boolean = isCanDrag

    override fun setOnClickListener(l: OnClickListener?) {
        mListener = l
        super.setOnClickListener(l)
    }

    /** 按下时X坐标 */
    private var mDownX = 0F
    /** 按下时Y坐标 */
    private var mDownY = 0F
    /** 是否移动 */
    private var isMove = false

    /** 按下时View的边距 */
    private var mDownViewRect: Rect? = null

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null){
            return false
        }
        if (!isCanDrag){
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x
                mDownY = event.y
                mDownViewRect = Rect(v.left, v.top, v.right, v.bottom)
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isMove) {
                    val deltaX = abs(mDownX - event.x)
                    val deltaY = abs(mDownY - event.y)
                    isMove = deltaX > 0.5 || deltaY > 0.5
                }
                // 移动图片
                offsetLeftAndRight((event.x - mDownX).toInt())
                offsetTopAndBottom((event.y - mDownY).toInt())
            }
            MotionEvent.ACTION_UP -> {
                if (isMove) {
                    if (mAdsorbType == VERTICAL) {
                        adsorbTopAndBottom(v)
                    } else if (mAdsorbType == HORIZONTAL) {
                        adsorbLeftAndRight(v)
                    }
                } else {
                    mListener?.onClick(v)
                }
                isMove = false
                mDownViewRect = null
            }
        }
        return true
    }

    /**
     * 上下吸边
     */
    private fun adsorbTopAndBottom(v: View) {
        /**
         * 1.判断起始位置在上/下半屏
         * 2.上半屏：
         *      3.1.滑动距离<半屏=吸顶
         *      3.2.滑动距离>半屏=吸底
         * 3.下半屏：
         *      4.1.滑动距离<半屏=吸底
         *      4.2.滑动距离>半屏=吸顶
         * 4.判断是否贴左或贴右，校正图标位置
         */

        val viewRect = mDownViewRect ?: return
        val downCenterY = (viewRect.bottom - viewRect.top) / 2 + viewRect.top
        val upCenterY = (v.bottom - v.top) / 2 + v.top

        if (isTouchFromTop(downCenterY)) {//初始中心点在边框上侧
            if (upCenterY > mParentHeigth / 2) {
                //滑动距离>半屏=吸底
                adsorbBottom(v)
            } else {
                //滑动距离<半屏=吸顶
                adsorbTop(v)
            }
        } else {//初始中心点在边框下侧
            if (upCenterY > mParentHeigth / 2) {
                //滑动距离>半屏=吸底
                adsorbBottom(v)
            } else {
                //滑动距离<半屏=吸顶
                adsorbTop(v)
            }
        }
        resetHorizontal(v)
    }

    /**
     * 左右吸边
     */
    private fun adsorbLeftAndRight(v: View) {
        /**
         * 1.判断起始位置在左/右半屏
         * 2.左半屏：
         *      3.1.滑动距离<半屏=吸左
         *      3.2.滑动距离>半屏=吸右
         * 3.右半屏：
         *      4.1.滑动距离<半屏=吸右
         *      4.2.滑动距离>半屏=吸左
         * 4.判断是否贴顶或贴底，校正图标位置
         */

        val viewRect = mDownViewRect ?: return
        val downCenterX = (viewRect.right - viewRect.left) / 2 + viewRect.left
        val upCenterX = (v.right - v.left) / 2 + v.left

        if (isTouchFromLeft(downCenterX)) {//初始中心点在边框左侧
            if (upCenterX > mParentWidth / 2) {
                //滑动距离>半屏  吸右
                adsorbRight(v)
            } else {
                //滑动距离<半屏  吸左
                adsorbLeft(v)
            }
        } else {//初始中心点在边框右侧
            if (upCenterX > mParentWidth / 2) {
                //滑动距离>半屏  吸右
                adsorbRight(v)
            } else {
                //滑动距离<半屏  吸左
                adsorbLeft(v)
            }
        }
        resetVertical(v)
    }

    /** 吸左 */
    private fun adsorbLeft(v: View) {
        animate().setInterpolator(DecelerateInterpolator()).setDuration(mAnimDuration)
            .x((v.marginStart + mParentPaddingStart).toFloat()).start()
        offsetLeftAndRight(-v.left + v.marginStart + mParentPaddingStart)
    }

    /** 吸右 */
    private fun adsorbRight(v: View) {
        animate().setInterpolator(DecelerateInterpolator()).setDuration(mAnimDuration)
            .x((mParentWidth - mViewWidth - v.marginEnd - mParentPaddingEnd).toFloat()).start()
        offsetLeftAndRight(mParentWidth - v.right - v.marginEnd - mParentPaddingEnd)
    }

    /** 吸上 */
    private fun adsorbTop(v: View) {
        animate().setInterpolator(DecelerateInterpolator()).setDuration(mAnimDuration)
            .y((v.marginTop + mParentPaddingTop).toFloat()).start()
        offsetTopAndBottom(-v.top + v.marginTop + mParentPaddingTop)
    }

    /** 吸下 */
    private fun adsorbBottom(v: View) {
        animate().setInterpolator(DecelerateInterpolator()).setDuration(mAnimDuration)
            .y((mParentHeigth - mViewHeigth - v.marginBottom - mParentPaddingBottom).toFloat()).start()
        offsetTopAndBottom(mParentHeigth - v.bottom - v.marginBottom - mParentPaddingBottom)
    }

    /**
     * 左右拖拽时，如果顶部或底部超出父控件区域，校正垂直位置贴边
     */
    private fun resetVertical(v: View) {
        if (v.top < v.marginTop + mParentPaddingTop) {
            adsorbTop(v)
        }
        if (v.bottom > mParentHeigth - v.marginBottom - mParentPaddingBottom){
            adsorbBottom(v)
        }
    }

    /**
     * 上下拖拽时，如果左右超出父控件区域，校正水平位置贴边
     */
    private fun resetHorizontal(v: View) {
        if (v.left < v.marginStart + mParentPaddingStart){
            adsorbLeft(v)
        }
        if (v.right > mParentWidth - v.marginEnd - mParentPaddingEnd){
            adsorbRight(v)
        }
    }

    /** 控件的中心X轴坐标[viewCenterX]是否在边框中线的左边 */
    private fun isTouchFromLeft(viewCenterX: Int): Boolean {
        return viewCenterX < (mParentWidth / 2)
    }

    /** 控件的中心Y轴坐标[viewCenterY]是否在边框中线的上侧 */
    private fun isTouchFromTop(viewCenterY: Int): Boolean {
        return viewCenterY < (mParentHeigth / 2)
    }

}