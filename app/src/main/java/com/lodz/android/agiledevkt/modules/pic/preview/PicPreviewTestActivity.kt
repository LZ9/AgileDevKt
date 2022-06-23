package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityPicPreviewBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.picker.preview.PreviewManager
import com.lodz.android.pandora.picker.preview.adapter.ImageViewHolder
import com.lodz.android.pandora.picker.preview.adapter.SimplePreviewAdapter
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter
import com.lodz.android.pandora.widget.vp2.ScaleInTransformer

/**
 * 图片预览测试类
 * Created by zhouL on 2018/12/14.
 */
class PicPreviewTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PicPreviewTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 起始页码 */
    private var mPosition = 0

    private val mBinding: ActivityPicPreviewBinding by bindingLayout(ActivityPicPreviewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.pic_preview)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    /** 创建预览器 */
    private fun <VH : RecyclerView.ViewHolder> openPreview(adapter: AbsRvAdapter<String, VH>, pageTransformer: ViewPager2.PageTransformer? = null){
        PreviewManager.create<String, VH>()
            .setPosition(mPosition)
            .setBackgroundColor(R.color.black)
            .setStatusBarColor(R.color.black)
            .setNavigationBarColor(R.color.black)
            .setPagerTextColor(R.color.white)
            .setPagerTextSize(14)
            .setShowPagerText(mBinding.showPagerSwitch.isChecked)
            .setPageTransformer(pageTransformer)
            .setAdapter(adapter)
            .build(Constant.IMG_URLS)
            .open(getContext())
    }


    override fun setListeners() {
        super.setListeners()

        // 加页码
        mBinding.plusBtn.setOnClickListener {
            mPosition++
            if (mPosition > Constant.IMG_URLS.size - 1) {
                mPosition = Constant.IMG_URLS.size - 1
            }
            mBinding.positionTv.text = getPositionStr()
        }

        // 减页码
        mBinding.minusBtn.setOnClickListener {
            mPosition--
            if (mPosition < 0) {
                mPosition = 0
            }
            mBinding.positionTv.text = getPositionStr()
        }

        // ImageView预览
        mBinding.imgBtn.setOnClickListener {
            if (mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(ScaleInTransformer())
            openPreview(object :SimplePreviewAdapter<String, ImageViewHolder>(getContext()){

                // TODO: 2022/6/22 预览器不能传入适配器，会有异常，需要修改
                override fun getViewHolder(frameLayout: FrameLayout): ImageViewHolder = ImageViewHolder(context, frameLayout)

                override fun onDisplayImg(context: Context, source: String, holder: ImageViewHolder) {
                    ImageLoader.create(context).loadUrl(source).setFitCenter().into(holder.imageView)
                }
            }, compositePageTransformer)
        }

        // PhotoView预览
        mBinding.photoBtn.setOnClickListener {
            if (!mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            openPreview(PhotoViewAdapter(getContext(), mBinding.scaleSwitch.isChecked))
        }

        // ScaleImageView预览
        mBinding.largeBtn.setOnClickListener {
            if (!mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            openPreview(SubsamplingScaleImageViewAdapter(getContext(), mBinding.scaleSwitch.isChecked))
        }

        // LongImageView预览
        mBinding.longBtn.setOnClickListener {
            if (mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }
            openPreview(LongImageViewAdapter(getContext()))
        }
    }

    override fun initData() {
        super.initData()
        mBinding.positionTv.text = getPositionStr()
        showStatusCompleted()
    }

    private fun getPositionStr() = (mPosition + 1).toString()
}