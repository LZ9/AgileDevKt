package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityPicPreviewBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.picker.preview.AbsImageView
import com.lodz.android.pandora.picker.preview.PreviewManager
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
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
    private fun <V : View> openPreview(view: AbsImageView<V, String>, pageTransformer: ViewPager2.PageTransformer? = null){
        PreviewManager.create<V, String>()
            .setPosition(mPosition)
            .setBackgroundColor(R.color.black)
            .setStatusBarColor(R.color.black)
            .setNavigationBarColor(R.color.black)
            .setPagerTextColor(R.color.white)
            .setPagerTextSize(14)
            .setShowPagerText(mBinding.showPagerSwitch.isChecked)
            .setPageTransformer(pageTransformer)
            .setImageView(view)
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
            openPreview(ImageViewImpl(getContext(), mBinding.scaleSwitch.isChecked, mBinding.clickCloseSwitch.isChecked), compositePageTransformer)
        }

        // PhotoView预览
        mBinding.photoBtn.setOnClickListener {
            if (!mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            openPreview(PhotoViewImpl(getContext(), mBinding.scaleSwitch.isChecked, mBinding.clickCloseSwitch.isChecked))
        }

        // ScaleImageView预览
        mBinding.largeBtn.setOnClickListener {
            if (!mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            openPreview(SubsamplingScaleImageViewImpl(getContext(), mBinding.scaleSwitch.isChecked, mBinding.clickCloseSwitch.isChecked))
        }

        // LongImageView预览
        mBinding.longBtn.setOnClickListener {
            if (mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }
            openPreview(LongImageView(getContext(), mBinding.scaleSwitch.isChecked, mBinding.clickCloseSwitch.isChecked))
        }
    }

    override fun initData() {
        super.initData()
        mBinding.positionTv.text = getPositionStr()
        showStatusCompleted()
    }

    private fun getPositionStr() = (mPosition + 1).toString()
}