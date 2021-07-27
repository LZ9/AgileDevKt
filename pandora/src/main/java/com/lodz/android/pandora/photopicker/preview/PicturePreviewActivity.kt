package com.lodz.android.pandora.photopicker.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.databinding.PandoraActivityPreviewBinding
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.snap.ViewPagerSnapHelper

/**
 * 图片预览页面
 * Created by zhouL on 2018/12/13.
 */
internal class PicturePreviewActivity<V : View, T : Any> : AbsActivity() {

    companion object {
        private var sPreviewBean: PreviewBean<*, *>? = null
        internal fun <V : View, T : Any> start(context: Context, previewBean: PreviewBean<V, T>, flags: List<Int>?) {
            synchronized(this) {
                if (sPreviewBean != null) {
                    return
                }
                sPreviewBean = previewBean
                val intent = Intent(context, PicturePreviewActivity::class.java)
                flags?.forEach {
                    intent.addFlags(it)
                }
                context.startActivity(intent)
            }
        }
    }

    private val mBinding: PandoraActivityPreviewBinding by bindingLayout(PandoraActivityPreviewBinding::inflate)

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

    override fun getAbsViewBindingLayout(): View = mBinding.root

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
        mBinding.pdrPreviewRv.layoutManager = layoutManager
        mBinding.pdrPreviewRv.setHasFixedSize(true)
        mBinding.pdrPreviewRv.adapter = mPdrAdapter
        mPdrSnapHelper = ViewPagerSnapHelper(bean.showPosition)
        mPdrSnapHelper.attachToRecyclerView(mBinding.pdrPreviewRv)
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

        // 设置背景控件颜色
        mBinding.pdrRootView.setBackgroundColor(getColorCompat(bean.backgroundColor))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setColor(window, getColorCompat(bean.statusBarColor))
            StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.navigationBarColor))
        }
        setPagerNum(bean.showPosition)
        // 配置页码提示
        mBinding.pdrPagerTv.visibility = if (bean.isShowPagerText) View.VISIBLE else View.GONE
        mBinding.pdrPagerTv.setTextColor(getColorCompat(bean.pagerTextColor))
        mBinding.pdrPagerTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.pagerTextSize.toFloat())

        mPdrAdapter.setData(list.toMutableList())
        mPdrAdapter.notifyDataSetChanged()
        mBinding.pdrPreviewRv.scrollToPosition(bean.showPosition)
    }

    /** 设置页码[position] */
    private fun setPagerNum(position: Int) {
        if (mPdrPreviewBean != null) {
            mBinding.pdrPagerTv.text = StringBuffer().append(position + 1).append(" / ").append(mPdrPreviewBean!!.sourceList.getSize())
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