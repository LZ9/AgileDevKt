package com.lodz.android.pandora.widget.rv.recycler

import android.content.Context
import android.util.TypedValue
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.pandora.R

/**
 * 简单实现的BaseLoadMoreRecyclerViewAdapter
 * Created by zhouL on 2018/11/20.
 */
abstract class SimpleLoadMoreRVAdapter<T>(context: Context) : BaseLoadMoreRVAdapter<T>(context) {

    /** 完成加载提示语 */
    private var mPdrFinishText = context.getString(R.string.pandora_load_finish_tips)
    /** 完成加载提示语大小 */
    private var mPdrFinishTextSizeSp = 12
    /** 完成加载提示语颜色 */
    @ColorRes
    private var mPdrFinishTextColor = android.R.color.black
    /** 完成加载背景色颜色 */
    @ColorRes
    private var mPdrFinishBackgroundColor = android.R.color.transparent

    /** 正在加载提示语 */
    private var mPdrLoadingMoreText = context.getString(R.string.pandora_loading_more_tips)
    /** 正在加载提示语大小 */
    private var mPdrLoadingMoreTextSizeSp = 12
    /** 正在加载提示语颜色 */
    @ColorRes
    private var mPdrLoadingMoreTextColor = android.R.color.black
    /** 正在加载背景色颜色 */
    @ColorRes
    private var mPdrLoadingMoreBackgroundColor = android.R.color.transparent
    /** 正在加载动画资源资源 */
    @DrawableRes
    private var mPdrIndeterminateDrawable = 0

    /** 加载失败提示语 */
    private var mPdrLoadFailText = context.getString(R.string.pandora_load_fail_tips)
    /** 加载失败提示语大小 */
    private var mPdrLoadFailTextSizeSp = 12
    /** 加载失败提示语颜色 */
    @ColorRes
    private var mPdrLoadFailTextColor = android.R.color.holo_red_light
    /** 加载失败背景色颜色 */
    @ColorRes
    private var mPdrLoadFailBackgroundColor = android.R.color.transparent

    override fun getLoadFinishLayoutId(): Int = R.layout.pandora_item_load_finish

    override fun getLoadingMoreLayoutId(): Int = R.layout.pandora_item_loading_more

    override fun getLoadFailLayoutId(): Int = R.layout.pandora_item_load_fail

    override fun showLoadFinish(holder: RecyclerView.ViewHolder) {
        if (isPdrGridLayoutManager) {
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = context.getScreenWidth()
        }

        holder.itemView.setBackgroundColor(context.getColorCompat(mPdrFinishBackgroundColor))

        val textView = holder.itemView.findViewById<TextView>(R.id.load_finish_tv)
        textView.text = mPdrFinishText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mPdrFinishTextSizeSp.toFloat())
        textView.setTextColor(context.getColorCompat(mPdrFinishTextColor))
    }

    override fun showLoadFail(holder: RecyclerView.ViewHolder) {
        if (isPdrGridLayoutManager) {
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = context.getScreenWidth()
        }

        holder.itemView.setBackgroundColor(context.getColorCompat(mPdrLoadFailBackgroundColor))

        val textView = holder.itemView.findViewById<TextView>(R.id.load_fail_tv)
        textView.text = mPdrLoadFailText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mPdrLoadFailTextSizeSp.toFloat())
        textView.setTextColor(context.getColorCompat(mPdrLoadFailTextColor))
    }

    override fun showLoadingMore(holder: RecyclerView.ViewHolder) {
        if (isPdrGridLayoutManager) {
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = context.getScreenWidth()
        }

        holder.itemView.setBackgroundColor(context.getColorCompat(mPdrLoadingMoreBackgroundColor))

        val textView = holder.itemView.findViewById<TextView>(R.id.loading_more_tv)
        textView.text = mPdrLoadingMoreText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mPdrLoadingMoreTextSizeSp.toFloat())
        textView.setTextColor(context.getColorCompat(mPdrLoadingMoreTextColor))

        val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.loading_pb)
        progressBar.isIndeterminate = true
        if (mPdrIndeterminateDrawable != 0) {
            progressBar.indeterminateDrawable = context.getDrawableCompat(mPdrIndeterminateDrawable)
            mPdrIndeterminateDrawable = 0//设置过以后就清空数据，避免展示异常
        }
    }


    /** 设置完成加载时的提示语[text] */
    fun setFinishText(text: String) {
        this.mPdrFinishText = text
    }

    /** 设置完成加载提示语文字大小[sizeSp]（单位SP） */
    fun setFinishTextSizeSp(sizeSp: Int) {
        this.mPdrFinishTextSizeSp = sizeSp
    }

    /** 设置完成加载提示语颜色[textColor] */
    fun setFinishTextColor(@ColorRes textColor: Int) {
        this.mPdrFinishTextColor = textColor
    }

    /** 设置完成加载背景色[backgroundColor] */
    fun setFinishBackgroundColor(@ColorRes backgroundColor: Int) {
        this.mPdrFinishBackgroundColor = backgroundColor
    }

    /** 设置正在加载提示语[text] */
    fun setLoadingMoreText(text: String) {
        this.mPdrLoadingMoreText = text
    }

    /** 设置正在加载文字大小[sizeSp]（单位SP） */
    fun setLoadingMoreTextSizeSp(sizeSp: Int) {
        this.mPdrLoadingMoreTextSizeSp = sizeSp
    }

    /** 设置正在加载文字颜色[textColor] */
    fun setLoadingMoreTextColor(@ColorRes textColor: Int) {
        this.mPdrLoadingMoreTextColor = textColor
    }

    /** 设置正在加载背景色[backgroundColor] */
    fun setLoadingMoreBackgroundColor(@ColorRes backgroundColor: Int) {
        this.mPdrLoadingMoreBackgroundColor = backgroundColor
    }

    /** 设置正在加载动画资源[resId] */
    fun setIndeterminateDrawable(@DrawableRes resId: Int) {
        this.mPdrIndeterminateDrawable = resId
    }

    /** 设置加载失败提示语[text] */
    fun setLoadFailText(text: String) {
        this.mPdrLoadFailText = text
    }

    /** 设置加载失败提示语大小[sizeSp]（单位SP） */
    fun setLoadFailTextSizeSp(sizeSp: Int) {
        this.mPdrLoadFailTextSizeSp = sizeSp
    }

    /** 设置加载失败提示语文字颜色[textColor] */
    fun setLoadFailTextColor(@ColorRes textColor: Int) {
        this.mPdrLoadFailTextColor = textColor
    }

    /** 设置加载失败提示语背景大小[backgroundColor] */
    fun setLoadFailBackgroundColor(@ColorRes backgroundColor: Int) {
        this.mPdrLoadFailBackgroundColor = backgroundColor
    }

}