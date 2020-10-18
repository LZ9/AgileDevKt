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
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController
import com.lodz.android.pandora.widget.rv.snap.ViewPagerSnapHelper

/**
 * 图片预览页面
 * Created by zhouL on 2018/12/13.
 */
internal class PicturePreviewActivity<V : View, T : Any> : AbsActivity() {

    companion object {
        private var sPreviewBean: PreviewBean<*, *>? = null
        internal fun <V : View, T : Any> start(context: Context, previewBean: PreviewBean<V, T>) {
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
    private val mPdrRootView by bindView<ViewGroup>(R.id.pdr_root_view)
    /** 翻页控件 */
    private val mPdrRecyclerView by bindView<RecyclerView>(R.id.pdr_preview_rv)
    /** 页码提示 */
    private val mPdrPagerTv by bindView<TextView>(R.id.pdr_pager_tv)

    /** 预览控制器 */
    private lateinit var mPdrPreviewController: PreviewController
    /** 预览数据 */
    private var mPdrPreviewBean: PreviewBean<V, T>? = null
    /** 适配器 */
    private lateinit var mPdrAdapter: PicturePagerAdapter<V, T>
    /** 滑动帮助类 */
    private lateinit var mPdrSnapHelper: ViewPagerSnapHelper

    @Suppress("UNCHECKED_CAST")
    override fun startCreate() {
        super.startCreate()
        mPdrPreviewBean = sPreviewBean as PreviewBean<V, T>
        mPdrPreviewController = PreviewControllerImpl(this)
    }

    override fun getAbsLayoutId(): Int = R.layout.pandora_activity_preview

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        val bean = mPdrPreviewBean
        if (bean == null) {
            finish()
            return
        }
        val view = bean.imgView
        if (view == null) {
            finish()
            return
        }
        initRecyclerView(bean, view)
    }

    /** 初始化RV */
    private fun initRecyclerView(bean: PreviewBean<V, T>, view: AbsImageView<V, T>) {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        mPdrAdapter = PicturePagerAdapter(getContext(), view, mPdrPreviewController)
        mPdrRecyclerView.layoutManager = layoutManager
        mPdrRecyclerView.setHasFixedSize(true)
        mPdrRecyclerView.adapter = mPdrAdapter
        mPdrSnapHelper = ViewPagerSnapHelper(bean.showPosition)
        mPdrSnapHelper.attachToRecyclerView(mPdrRecyclerView)
    }

    override fun setListeners() {
        super.setListeners()
        mPdrSnapHelper.setOnPageChangeListener { position ->
            setPagerNum(position)
        }
    }

    override fun initData() {
        super.initData()
        val bean = mPdrPreviewBean
        if (bean == null) {
            finish()
            return
        }
        val list = bean.sourceList
        if (list.isNullOrEmpty()) {
            finish()
            return
        }

        mPdrRootView.setBackgroundColor(getColorCompat(bean.backgroundColor))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setColor(window, getColorCompat(bean.statusBarColor))
            StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.navigationBarColor))
        }
        setPagerNum(bean.showPosition)
        mPdrPagerTv.visibility = if (bean.isShowPagerText) View.VISIBLE else View.GONE
        mPdrPagerTv.setTextColor(getColorCompat(bean.pagerTextColor))
        mPdrPagerTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.pagerTextSize.toFloat())

        mPdrAdapter.setData(list.toMutableList())
        mPdrAdapter.notifyDataSetChanged()
        mPdrRecyclerView.scrollToPosition(bean.showPosition)
    }

    /** 设置页码[position] */
    private fun setPagerNum(position: Int) {
        if (mPdrPreviewBean != null) {
            mPdrPagerTv.text = StringBuffer().append(position + 1).append(" / ").append(mPdrPreviewBean!!.sourceList.getSize())
        }
    }

    private class PreviewControllerImpl(val activity: Activity) : PreviewController {
        override fun close() {
            activity.finish()
        }
    }

    override fun finish() {
        mPdrAdapter.release()
        mPdrPreviewBean?.clear()
        mPdrPreviewBean = null
        sPreviewBean?.clear()
        sPreviewBean = null
        super.finish()
    }
}