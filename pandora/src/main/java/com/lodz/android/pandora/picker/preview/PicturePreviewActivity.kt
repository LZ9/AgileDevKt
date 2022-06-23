package com.lodz.android.pandora.picker.preview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.databinding.PandoraActivityPreviewBinding
import com.lodz.android.pandora.event.PicturePreviewFinishEvent
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 图片预览页面
 * Created by zhouL on 2018/12/13.
 */
internal class PicturePreviewActivity<T : Any, VH : RecyclerView.ViewHolder> : AbsActivity() {

    companion object {
        private var sPreviewBean: PreviewBean<*, *>? = null
        internal fun <T : Any, VH : RecyclerView.ViewHolder> start(context: Context, previewBean: PreviewBean<T, VH>, flags: List<Int>?) {
            synchronized(PreviewBean::class.java) {
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

    /** 预览数据 */
    @Suppress("UNCHECKED_CAST")
    private val mPdrPreviewBean: PreviewBean<T, VH>? by lazy { sPreviewBean as PreviewBean<T, VH> }
    /** 适配器 */
    private lateinit var mPdrAdapter: AbsRvAdapter<T, VH>

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        val bean = mPdrPreviewBean
        if (bean == null) {
            finish()
            return
        }
        val adapter = bean.adapter
        if (adapter == null) {
            finish()
            return
        }
        initViewPager(adapter, bean.pageTransformer)
    }

    private fun initViewPager(adapter: AbsRvAdapter<T, VH>, pageTransformer: ViewPager2.PageTransformer?) {
        mPdrAdapter = adapter
        mBinding.pdrPreviewVp2.adapter = mPdrAdapter
        mBinding.pdrPreviewVp2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mBinding.pdrPreviewVp2.isUserInputEnabled = true
        mBinding.pdrPreviewVp2.setPageTransformer(pageTransformer)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.pdrPreviewVp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setPagerNum(position)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
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
        mBinding.pdrRootView.setBackgroundColor(bean.backgroundColor)
        StatusBarUtil.setColor(window, bean.statusBarColor)
        StatusBarUtil.setNavigationBarColor(window, bean.navigationBarColor)
        setPagerNum(bean.showPosition)
        // 配置页码提示
        mBinding.pdrPagerTv.visibility = if (bean.isShowPagerText) View.VISIBLE else View.GONE
        mBinding.pdrPagerTv.setTextColor(bean.pagerTextColor)
        mBinding.pdrPagerTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.pagerTextSize.toFloat())

        mPdrAdapter.setData(list.toMutableList())
        mPdrAdapter.notifyDataSetChanged()
        mBinding.pdrPreviewVp2.currentItem = bean.showPosition
    }

    /** 设置页码[position] */
    private fun setPagerNum(position: Int) {
        if (mPdrPreviewBean != null) {
            mBinding.pdrPagerTv.text = StringBuffer().append(position + 1).append(" / ").append(mPdrPreviewBean!!.sourceList.getSize())
        }
    }

    override fun finish() {
        release()
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    private fun release() {
        mPdrPreviewBean?.clear()
        sPreviewBean?.clear()
        sPreviewBean = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPhotoPickerFinishEvent(event: PicturePreviewFinishEvent) {
        if (!isFinishing) {
            finish()
        }
    }
}