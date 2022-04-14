package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityPicPreviewBinding
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.picker.preview.AbsImageView
import com.lodz.android.pandora.picker.preview.PreviewManager
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.custom.LongImageView
import java.io.File

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

    override fun setListeners() {
        super.setListeners()

        // 加页码
        mBinding.plusBtn.setOnClickListener {
            mPosition++
            if (mPosition > Constant.IMG_URLS.size - 1) {
                mPosition = Constant.IMG_URLS.size - 1
            }
            mBinding.positionTv.text = (mPosition + 1).toString()
        }

        // 减页码
        mBinding.minusBtn.setOnClickListener {
            mPosition--
            if (mPosition < 0) {
                mPosition = 0
            }
            mBinding.positionTv.text = (mPosition + 1).toString()
        }

        // ImageView预览
        mBinding.imgBtn.setOnClickListener {
            if (mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }
            PreviewManager.create<ImageView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mBinding.showPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<ImageView, String>(mBinding.scaleSwitch.isChecked){
                        override fun onCreateView(context: Context, isScale: Boolean): ImageView {
                            val img = ImageView(context)
                            img.scaleType = ImageView.ScaleType.CENTER_INSIDE
                            return img
                        }

                        override fun onDisplayImg(context: Context, source: String, view: ImageView) {
                            ImageLoader.create(context).loadUrl(source).setFitCenter().into(view)
                        }

                        override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: ImageView, item: String, position: Int, controller: PreviewController) {
                            super.onClickImpl(viewHolder, view, item, position, controller)
                            view.setOnClickListener {
                                if (mBinding.clickCloseSwitch.isChecked) {
                                    controller.close()
                                } else {
                                    toastShort(getString(R.string.preview_click_tips, position.toString()))
                                }
                            }
                        }

                        override fun onLongClickImpl(viewHolder: RecyclerView.ViewHolder, view: ImageView, item: String, position: Int, controller: PreviewController) {
                            super.onLongClickImpl(viewHolder, view, item, position, controller)
                            view.setOnLongClickListener {
                                toastShort(getString(R.string.preview_long_click_tips, position.toString()))
                                return@setOnLongClickListener true
                            }
                        }
                    })
                    .build(Constant.IMG_URLS)
                    .open(getContext())
        }

        // PhotoView预览
        mBinding.photoBtn.setOnClickListener {
            if (!mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            PreviewManager.create<PhotoView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mBinding.showPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<PhotoView, String>(mBinding.scaleSwitch.isChecked){
                        override fun onCreateView(context: Context, isScale: Boolean): PhotoView {
                            val img = PhotoView(context)
                            img.scaleType = ImageView.ScaleType.CENTER_INSIDE
                            img.setZoomable(isScale)
                            return img
                        }

                        override fun onDisplayImg(context: Context, source: String, view: PhotoView) {
                            ImageLoader.create(context).loadUrl(source).setFitCenter().into(view)
                        }

                        override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: PhotoView, item: String, position: Int, controller: PreviewController) {
                            super.onClickImpl(viewHolder, view, item, position, controller)
                            view.setOnClickListener {
                                if (mBinding.clickCloseSwitch.isChecked) {
                                    controller.close()
                                } else {
                                    toastShort(getString(R.string.preview_click_tips, position.toString()))
                                }
                            }
                        }

                        override fun onLongClickImpl(viewHolder: RecyclerView.ViewHolder, view: PhotoView, item: String, position: Int, controller: PreviewController) {
                            super.onLongClickImpl(viewHolder, view, item, position, controller)
                            view.setOnLongClickListener {
                                toastShort(getString(R.string.preview_long_click_tips, position.toString()))
                                return@setOnLongClickListener true
                            }
                        }

                        override fun onViewDetached(view: PhotoView, isScale: Boolean) {
                            super.onViewDetached(view, isScale)
                            if (isScale){
                                view.attacher.update()
                            }
                        }
                    })
                    .build(Constant.IMG_URLS)
                    .open(getContext())
        }

        // ScaleImageView预览
        mBinding.largeBtn.setOnClickListener {
            if (!mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            PreviewManager.create<SubsamplingScaleImageView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mBinding.showPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<SubsamplingScaleImageView, String>(mBinding.scaleSwitch.isChecked){
                        override fun onCreateView(context: Context, isScale: Boolean): SubsamplingScaleImageView {
                            val img = SubsamplingScaleImageView(context)
                            img.setImage(ImageSource.resource(R.drawable.ic_launcher))
                            img.isZoomEnabled = isScale
                            return img
                        }

                        override fun onDisplayImg(context: Context, source: String, view: SubsamplingScaleImageView) {
                            ImageLoader.create(context).loadUrl(source)
                                .download(object :RequestListener<File>{
                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                                        runOnMain {
                                            view.setImage(ImageSource.resource(R.drawable.ic_launcher))
                                        }
                                        return false
                                    }

                                    override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        runOnMain {
                                            if (resource != null){
                                                view.setImage(ImageSource.uri(Uri.fromFile(resource)))
                                            }
                                        }
                                        return false
                                    }
                                })
                        }

                        override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: SubsamplingScaleImageView, item: String, position: Int, controller: PreviewController) {
                            super.onClickImpl(viewHolder, view, item, position, controller)
                            view.setOnClickListener {
                                if (mBinding.clickCloseSwitch.isChecked) {
                                    controller.close()
                                } else {
                                    toastShort(getString(R.string.preview_click_tips, position.toString()))
                                }
                            }
                        }

                        override fun onLongClickImpl(viewHolder: RecyclerView.ViewHolder, view: SubsamplingScaleImageView, item: String, position: Int, controller: PreviewController) {
                            super.onLongClickImpl(viewHolder, view, item, position, controller)
                            view.setOnLongClickListener {
                                toastShort(getString(R.string.preview_long_click_tips, position.toString()))
                                return@setOnLongClickListener true
                            }
                        }

                        override fun onViewDetached(view: SubsamplingScaleImageView, isScale: Boolean) {
                            super.onViewDetached(view, isScale)
                            if (isScale){
                                view.resetScaleAndCenter()
                            }
                        }
                    })
                    .build(Constant.IMG_URLS)
                    .open(getContext())
        }

        // LongImageView预览
        mBinding.longBtn.setOnClickListener {
            if (mBinding.scaleSwitch.isChecked){
                mBinding.scaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }
            PreviewManager.create<LongImageView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mBinding.showPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<LongImageView, String>(mBinding.scaleSwitch.isChecked){
                        override fun onCreateView(context: Context, isScale: Boolean): LongImageView {
                            val img = LongImageView(context)
                            img.setPlaceholderRes(R.drawable.ic_launcher)
                            img.setPlaceholderScaleType(ImageView.ScaleType.CENTER)
                            return img
                        }

                        override fun onDisplayImg(context: Context, source: String, view: LongImageView) {
                            ImageLoader.create(context).loadUrl(source)
                                .download(object :RequestListener<File>{
                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                                        return false
                                    }

                                    override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        runOnMain {
                                            if (resource != null){
                                                view.setImageFile(resource)
                                            }
                                        }
                                        return false
                                    }
                                })
                        }

                        override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: LongImageView, item: String, position: Int, controller: PreviewController) {
                            super.onClickImpl(viewHolder, view, item, position, controller)
                            view.setOnClickListener {
                                if (mBinding.clickCloseSwitch.isChecked) {
                                    controller.close()
                                } else {
                                    toastShort(getString(R.string.preview_click_tips, position.toString()))
                                }
                            }
                        }

                        override fun onLongClickImpl(viewHolder: RecyclerView.ViewHolder, view: LongImageView, item: String, position: Int, controller: PreviewController) {
                            super.onLongClickImpl(viewHolder, view, item, position, controller)
                            view.setOnLongClickListener {
                                toastShort(getString(R.string.preview_long_click_tips, position.toString()))
                                return@setOnLongClickListener true
                            }
                        }
                    })
                    .build(Constant.IMG_URLS)
                    .open(getContext())
        }
    }

    override fun initData() {
        super.initData()
        mBinding.positionTv.text = (mPosition + 1).toString()
        showStatusCompleted()
    }
}