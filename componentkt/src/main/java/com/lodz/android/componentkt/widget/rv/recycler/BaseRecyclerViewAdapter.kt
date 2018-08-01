package com.lodz.android.componentkt.widget.rv.recycler

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.lodz.android.componentkt.widget.rv.animation.*

/**
 * RecyclerView基类适配器
 * Created by zhouL on 2018/6/28.
 */
abstract class BaseRecyclerViewAdapter<T>(protected val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        /** 淡入  */
        const val ALPHA_IN = 1
        /** 缩放  */
        const val SCALE_IN = 2
        /** 底部进入  */
        const val SLIDE_IN_BOTTOM = 3
        /** 左侧进入  */
        const val SLIDE_IN_LEFT = 4
        /** 右侧进入  */
        const val SLIDE_IN_RIGHT = 5
    }

    @IntDef(ALPHA_IN, SCALE_IN, SLIDE_IN_BOTTOM, SLIDE_IN_LEFT, SLIDE_IN_RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class AnimationType

    /** 数据列表 */
    private var mData: MutableList<T>? = null
    /** item点击 */
    private var mOnItemClickListener: ((RecyclerView.ViewHolder, T, Int) -> Unit)? = null
    /** item长按 */
    private var mOnItemLongClickListener: ((RecyclerView.ViewHolder, T, Int) -> Unit)? = null

    /** 动画加速器 */
    private val mInterpolator = LinearInterpolator()
    /** item加载动画的最后位置 */
    private var mLastPosition = 0
    /** 用户定义的item加载动画的开始位置 */
    private var mCustomStarPosition = 0
    /** 是否需要item加载动画 */
    private var isOpenItemAnim = false
    /** 当前动画类型 */
    @AnimationType
    private var mCurrentAnimationType = ALPHA_IN
    /** 自定义动画 */
    private var mCustomAnimation: BaseAnimation? = null

    /** 根据[position]获取数据 */
    fun getItem(position: Int): T? {
        if (mData == null || mData!!.size == 0) {
            return null
        }
        try {
            return mData!!.get(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setItemClick(holder, position)
        setItemLongClick(holder, position)
        onBind(holder, position)
    }

    /** 绑定数据 */
    abstract fun onBind(holder: RecyclerView.ViewHolder, position: Int)

    /** 设置点击事件 */
    protected fun setItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val item = getItem(position)
            if (position >= 0 && mOnItemClickListener != null && item != null) {
                mOnItemClickListener!!.invoke(holder, item, position)
            }
        }
    }

    /** 设置长按事件 */
    protected fun setItemClick(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val item = getItem(position)
            if (position >= 0 && mOnItemLongClickListener != null && item != null) {
                mOnItemLongClickListener!!.invoke(holder, item, position)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        addAnimation(holder)
    }

    /** 添加item加载动画 */
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (mData != null && mData!!.size > 0 && mData!!.size < mLastPosition) {//重新设置了数据
            mLastPosition = mCustomStarPosition
        }
        if (!isOpenItemAnim || holder.layoutPosition <= mLastPosition) {//不打开item动画 || 已经加载过了
            return
        }
        var animation = getAnimationByType(mCurrentAnimationType)
        if (mCustomAnimation != null) {
            animation = mCustomAnimation!!
        }

        for (anim in animation.getAnimators(holder.itemView)) {
            beginAnim(anim, animation)
        }
        mLastPosition = holder.layoutPosition
    }

    /** 根据动画类型[animationType]获取动画 */
    @SuppressLint("SwitchIntDef")
    private fun getAnimationByType(@AnimationType animationType: Int) = when (animationType) {
        SCALE_IN -> ScaleInAnimation()
        SLIDE_IN_BOTTOM -> SlideInBottomAnimation()
        SLIDE_IN_LEFT -> SlideInLeftAnimation()
        SLIDE_IN_RIGHT -> SlideInRightAnimation()
        else -> AlphaInAnimation()
    }

    /** 开始item加载动画 */
    private fun beginAnim(animator: Animator, baseAnimation: BaseAnimation) {
        animator.setDuration(baseAnimation.getDuration().toLong()).start()
        animator.interpolator = mInterpolator
    }

    /** 是否打开item加载动画 */
    fun isOpenItemAnim() = isOpenItemAnim

    /** 设置是否打开item加载动画 */
    fun setOpenItemAnim(isOpen: Boolean) {
        isOpenItemAnim = isOpen
    }

    /** 设置item动画开始的位置[position] */
    fun setItemAnimStartPosition(@IntRange(from = 0) position: Int) {
        mCustomStarPosition = position
        mLastPosition = position
    }

    /** 重置item动画 */
    fun resetItemAnimPosition() {
        setItemAnimStartPosition(mCustomStarPosition)
    }

    /** 设置自定义动画 */
    fun setBaseAnimation(animation: BaseAnimation) {
        mCustomAnimation = animation
    }

    /** 设置默认的动画类型[animationType] */
    fun setAnimationType(@AnimationType animationType: Int) {
        mCurrentAnimationType = animationType
    }

    override fun getItemCount(): Int {
        return getDataSize()
    }

    /** 获取数据长度 */
    protected fun getDataSize(): Int {
        if (mData == null) {
            return 0
        }
        return mData!!.size
    }

    /** 设置数据列表[data] */
    fun setData(data: MutableList<T>) {
        this.mData = data
    }

    /** 获取数据列表[data] */
    fun getData(): MutableList<T>? = mData

    /** 在onCreateViewHolder方法中根据[layoutId]获取View */
    protected fun getLayoutView(parent: ViewGroup, @LayoutRes layoutId: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context).inflate(layoutId, parent, attachToRoot)

    /** 设置[itemView]的宽度值[width] */
    protected fun setItemViewWidth(itemView: View, width: Int) {
        val layoutParams = itemView.layoutParams
        layoutParams.width = width
        itemView.layoutParams = layoutParams
    }

    /** 设置[itemView]的高度值[width] */
    protected fun setItemViewHeight(itemView: View, height: Int) {
        val layoutParams = itemView.layoutParams
        layoutParams.height = height
        itemView.layoutParams = layoutParams
    }

    /** 带动画的删除位置为[position]的item并刷新数据 */
    fun notifyItemRemovedChanged(position: Int) {
        if (getDataSize() == 0) {
            return
        }
        mData!!.removeAt(position)
        notifyItemRemoved(position)
        if (position != mData!!.size) {// 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, mData!!.size - position)
        }
    }

    /** 设置点击事件监听器 */
    fun setOnItemClickListener(listener: (viewHolder: RecyclerView.ViewHolder, item: T, position: Int) -> Unit) {
        mOnItemClickListener = listener
    }

    /** 设置长按事件监听器 */
    fun setOnItemLongClickListener(listener: (viewHolder: RecyclerView.ViewHolder, item: T, position: Int) -> Unit) {
        mOnItemLongClickListener = listener
    }

}