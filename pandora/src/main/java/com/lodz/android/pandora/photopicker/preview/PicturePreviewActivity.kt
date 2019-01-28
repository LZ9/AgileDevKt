package com.lodz.android.pandora.photopicker.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.photopicker.contract.OnImgLoader
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController
import com.lodz.android.pandora.widget.rv.snap.ViewPagerSnapHelper

/**
 * 图片预览页面
 * Created by zhouL on 2018/12/13.
 */
internal class PicturePreviewActivity<T : Any> : AbsActivity() {

    companion object {
        private var sPreviewBean: PreviewBean<*>? = null
        internal fun <T : Any> start(context: Context, previewBean: PreviewBean<T>) {
            synchronized(this) {
                if (sPreviewBean != null) {
                    return
                }
                sPreviewBean = previewBean
                val intent = Intent(context, PicturePreviewActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    /** 背景控件 */
    private val mRootView by bindView<ViewGroup>(R.id.root_view)
    /** 翻页控件 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 页码提示 */
    private val mPagerTv by bindView<TextView>(R.id.pager_tv)

    /** 预览控制器 */
    private lateinit var mPreviewController: PreviewController
    /** 预览数据 */
    private var mPreviewBean: PreviewBean<T>? = null
    /** 适配器 */
    private lateinit var mAdapter: PicturePagerAdapter<T>
    /** 滑动帮助类 */
    private lateinit var mSnapHelper: ViewPagerSnapHelper

    @Suppress("UNCHECKED_CAST")
    override fun startCreate() {
        super.startCreate()
        mPreviewBean = sPreviewBean as PreviewBean<T>
        mPreviewController = PreviewControllerImpl(this)
    }

    override fun getAbsLayoutId(): Int = R.layout.pandora_activity_preview

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        val bean = mPreviewBean
        if (bean == null) {
            finish()
            return
        }
        val imgLoader = bean.imgLoader
        if (imgLoader == null) {
            finish()
            return
        }
        initRecyclerView(bean, imgLoader)
    }

    /** 初始化RV */
    private fun initRecyclerView(bean: PreviewBean<T>, imgLoader: OnImgLoader<T>) {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        mAdapter = PicturePagerAdapter(getContext(), bean.isScale, imgLoader)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
        mSnapHelper = ViewPagerSnapHelper(bean.showPosition)
        mSnapHelper.attachToRecyclerView(mRecyclerView)
    }

    override fun setListeners() {
        super.setListeners()
        mSnapHelper.setOnPageChangeListener { position ->
            setPagerNum(position)
        }

        mAdapter.setOnImgClickListener { viewHolder, item, position ->
            mPreviewBean?.clickListener?.onClick(viewHolder.itemView.context, item, position, mPreviewController)
        }

        mAdapter.setOnImgLongClickListener { viewHolder, item, position ->
            mPreviewBean?.longClickListener?.onLongClick(viewHolder.itemView.context, item, position, mPreviewController)
        }
    }

    override fun initData() {
        super.initData()
        val bean = mPreviewBean
        if (bean == null) {
            finish()
            return
        }
        val list = bean.sourceList
        if (list.isNullOrEmpty()) {
            finish()
            return
        }

        mRootView.setBackgroundColor(getColorCompat(bean.backgroundColor))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setColor(window, getColorCompat(bean.statusBarColor))
            StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.navigationBarColor))
        }
        setPagerNum(bean.showPosition)
        mPagerTv.visibility = if (bean.isShowPagerText) View.VISIBLE else View.GONE
        mPagerTv.setTextColor(getColorCompat(bean.pagerTextColor))
        mPagerTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.pagerTextSize.toFloat())

        mAdapter.setData(list.toMutableList())
        mAdapter.notifyDataSetChanged()
        mRecyclerView.scrollToPosition(bean.showPosition)
    }

    /** 设置页码[position] */
    private fun setPagerNum(position: Int) {
        if (mPreviewBean != null) {
            mPagerTv.text = StringBuffer().append(position + 1).append(" / ").append(mPreviewBean!!.sourceList.getSize())
        }
    }

    private class PreviewControllerImpl(val activity: Activity) : PreviewController {
        override fun close() {
            activity.finish()
        }
    }

    override fun finish() {
        mAdapter.release()
        mPreviewBean?.clear()
        mPreviewBean = null
        sPreviewBean?.clear()
        sPreviewBean = null
        super.finish()
    }
}