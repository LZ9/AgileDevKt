package com.lodz.android.componentkt.widget.rv.recycler

import android.content.Context
import android.util.TypedValue
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.componentkt.R
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.getScreenWidth

/**
 * 简单实现的BaseLoadMoreRecyclerViewAdapter
 * Created by zhouL on 2018/11/20.
 */
abstract class SimpleLoadMoreRVAdapter<T>(context: Context) : BaseLoadMoreRVAdapter<T>(context) {

    /** 完成加载提示语 */
    private var mFinishText = context.getString(R.string.componentkt_load_finish_tips)
    /** 完成加载提示语大小 */
    private var mFinishTextSizeSp = 12
    /** 完成加载提示语颜色 */
    @ColorRes
    private var mFinishTextColor = android.R.color.black
    /** 完成加载背景色颜色 */
    @ColorRes
    private var mFinishBackgroundColor = android.R.color.transparent

    /** 正在加载提示语 */
    private var mLoadingMoreText = context.getString(R.string.componentkt_loading_more_tips)
    /** 正在加载提示语大小 */
    private var mLoadingMoreTextSizeSp = 12
    /** 正在加载提示语颜色 */
    @ColorRes
    private var mLoadingMoreTextColor = android.R.color.black
    /** 正在加载背景色颜色 */
    @ColorRes
    private var mLoadingMoreBackgroundColor = android.R.color.transparent
    /** 正在加载动画资源资源 */
    @DrawableRes
    private var mIndeterminateDrawable = 0

    /** 加载失败提示语 */
    private var mLoadFailText = context.getString(R.string.componentkt_load_fail_tips)
    /** 加载失败提示语大小 */
    private var mLoadFailTextSizeSp = 12
    /** 加载失败提示语颜色 */
    @ColorRes
    private var mLoadFailTextColor = android.R.color.holo_red_light
    /** 加载失败背景色颜色 */
    @ColorRes
    private var mLoadFailBackgroundColor = android.R.color.transparent

    override fun getLoadFinishLayoutId(): Int = R.layout.componentkt_item_load_finish

    override fun getLoadingMoreLayoutId(): Int = R.layout.componentkt_item_loading_more

    override fun getLoadFailLayoutId(): Int = R.layout.componentkt_item_load_fail

    override fun showLoadFinish(holder: RecyclerView.ViewHolder) {
        if (isGridLayoutManager) {
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = context.getScreenWidth()
        }

        holder.itemView.setBackgroundColor(context.getColorCompat(mFinishBackgroundColor))

        val textView = holder.itemView.findViewById<TextView>(R.id.load_finish_tv)
        textView.text = mFinishText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mFinishTextSizeSp.toFloat())
        textView.setTextColor(context.getColorCompat(mFinishTextColor))
    }

    override fun showLoadFail(holder: RecyclerView.ViewHolder) {
        if (isGridLayoutManager) {
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = context.getScreenWidth()
        }

        holder.itemView.setBackgroundColor(context.getColorCompat(mLoadFailBackgroundColor))

        val textView = holder.itemView.findViewById<TextView>(R.id.load_fail_tv)
        textView.text = mLoadFailText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mLoadFailTextSizeSp.toFloat())
        textView.setTextColor(context.getColorCompat(mLoadFailTextColor))
    }

    override fun showLoadingMore(holder: RecyclerView.ViewHolder) {
        if (isGridLayoutManager) {
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = context.getScreenWidth()
        }

        holder.itemView.setBackgroundColor(context.getColorCompat(mLoadingMoreBackgroundColor))

        val textView = holder.itemView.findViewById<TextView>(R.id.loading_more_tv)
        textView.text = mLoadingMoreText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mLoadingMoreTextSizeSp.toFloat())
        textView.setTextColor(context.getColorCompat(mLoadingMoreTextColor))

        val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.loading_pb)
        progressBar.isIndeterminate = true
        if (mIndeterminateDrawable != 0) {
            progressBar.indeterminateDrawable = context.getDrawableCompat(mIndeterminateDrawable)
            mIndeterminateDrawable = 0//设置过以后就清空数据，避免展示异常
        }
    }


    /** 设置完成加载时的提示语[text] */
    fun setFinishText(text: String) {
        this.mFinishText = text
    }

    /** 设置完成加载提示语文字大小[sizeSp]（单位SP） */
    fun setFinishTextSizeSp(sizeSp: Int) {
        this.mFinishTextSizeSp = sizeSp
    }

    /** 设置完成加载提示语颜色[textColor] */
    fun setFinishTextColor(@ColorRes textColor: Int) {
        this.mFinishTextColor = textColor
    }

    /** 设置完成加载背景色[backgroundColor] */
    fun setFinishBackgroundColor(@ColorRes backgroundColor: Int) {
        this.mFinishBackgroundColor = backgroundColor
    }

    /** 设置正在加载提示语[text] */
    fun setLoadingMoreText(text: String) {
        this.mLoadingMoreText = text
    }

    /** 设置正在加载文字大小[sizeSp]（单位SP） */
    fun setLoadingMoreTextSizeSp(sizeSp: Int) {
        this.mLoadingMoreTextSizeSp = sizeSp
    }

    /** 设置正在加载文字颜色[textColor] */
    fun setLoadingMoreTextColor(@ColorRes textColor: Int) {
        this.mLoadingMoreTextColor = textColor
    }

    /** 设置正在加载背景色[backgroundColor] */
    fun setLoadingMoreBackgroundColor(@ColorRes backgroundColor: Int) {
        this.mLoadingMoreBackgroundColor = backgroundColor
    }

    /** 设置正在加载动画资源[resId] */
    fun setIndeterminateDrawable(@DrawableRes resId: Int) {
        this.mIndeterminateDrawable = resId
    }

    /** 设置加载失败提示语[text] */
    fun setLoadFailText(text: String) {
        this.mLoadFailText = text
    }

    /** 设置加载失败提示语大小[sizeSp]（单位SP） */
    fun setLoadFailTextSizeSp(sizeSp: Int) {
        this.mLoadFailTextSizeSp = sizeSp
    }

    /** 设置加载失败提示语文字颜色[textColor] */
    fun setLoadFailTextColor(@ColorRes textColor: Int) {
        this.mLoadFailTextColor = textColor
    }

    /** 设置加载失败提示语背景大小[backgroundColor] */
    fun setLoadFailBackgroundColor(@ColorRes backgroundColor: Int) {
        this.mLoadFailBackgroundColor = backgroundColor
    }

}