package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController
import com.lodz.android.pandora.photopicker.preview.AbsImageView
import com.lodz.android.pandora.photopicker.preview.PreviewManager
import com.lodz.android.pandora.widget.custom.LongImageView
import kotlinx.coroutines.GlobalScope
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

    private val IMG_URLS = arrayOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135406740&di=ad56c6b92e5d9888a04f0b724e5219d0&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F3801213fb80e7beca9004ec5252eb9389b506b38.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135426954&di=45834427b6f8ec30f1d7e1d99f59ee5c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0b7b02087bf40ad1cd0f99c55d2c11dfa9ecce29.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135506833&di=6b22dd2085f18b3643fe62b0f8b8955f&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F242dd42a2834349b8d289fafcbea15ce36d3beea.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135457903&di=e107c45dd449126ae54f0f665c558d05&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Df49943efb8119313d34ef7f30d5166a2%2Fb17eca8065380cd736f92fc0ab44ad345982813c.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135473894&di=27b040e674c4f9ac8b499f38612cab39&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd788d43f8794a4c2fc3e95eb07f41bd5ac6e39d4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135496262&di=5cef907ceff298c8d5d6c79841a72696&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201409%2F07%2F20140907224542_h4HvW.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135447478&di=90ddcac4604965af5d9bc744237a27aa&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd52a2834349b033b1c4bcdcf1fce36d3d439bde7.jpg",
            "http://bmob-cdn-15177.b0.upaiyun.com/2018/08/23/8fa7f1c2404bafbd808bde10ff072ceb.jpg")

    /** 缩放按钮 */
    private val mScaleSwitch by bindView<Switch>(R.id.scale_switch)
    /** 显示页码按钮 */
    private val mShowPagerSwitch by bindView<Switch>(R.id.show_pager_switch)
    /** 点击关闭按钮 */
    private val mClickCloseSwitch by bindView<Switch>(R.id.click_close_switch)
    /** 加页码 */
    private val mPlusBtn by bindView<TextView>(R.id.plus_btn)
    /** 减页码 */
    private val mMinusBtn by bindView<TextView>(R.id.minus_btn)
    /** 起始页码 */
    private val mPositionTv by bindView<TextView>(R.id.position_tv)

    /** ImageView预览 */
    private val mImgBtn by bindView<MaterialButton>(R.id.img_btn)
    /** ImageView预览 */
    private val mPhotoBtn by bindView<MaterialButton>(R.id.photo_btn)
    /** ScaleImageView预览 */
    private val mLargeBtn by bindView<MaterialButton>(R.id.large_btn)
    /** LongImageView预览 */
    private val mLongBtn by bindView<MaterialButton>(R.id.long_btn)

    /** 起始页码 */
    private var mPosition = 0

    override fun getLayoutId(): Int = R.layout.activity_pic_preview

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

        mPlusBtn.setOnClickListener {
            mPosition++
            if (mPosition > IMG_URLS.size - 1) {
                mPosition = IMG_URLS.size - 1
            }
            mPositionTv.text = (mPosition + 1).toString()
        }

        mMinusBtn.setOnClickListener {
            mPosition--
            if (mPosition < 0) {
                mPosition = 0
            }
            mPositionTv.text = (mPosition + 1).toString()
        }

        mImgBtn.setOnClickListener {
            if (mScaleSwitch.isChecked){
                mScaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }
            PreviewManager.create<ImageView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mShowPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<ImageView, String>(mScaleSwitch.isChecked){
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
                                if (mClickCloseSwitch.isChecked) {
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
                    .build(IMG_URLS)
                    .open(getContext())
        }

        mPhotoBtn.setOnClickListener {
            if (!mScaleSwitch.isChecked){
                mScaleSwitch.isChecked = true
                toastShort(R.string.preview_only_scale)
            }
            PreviewManager.create<PhotoView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mShowPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<PhotoView, String>(mScaleSwitch.isChecked){
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
                                if (mClickCloseSwitch.isChecked) {
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
                    .build(IMG_URLS)
                    .open(getContext())
        }

        mLargeBtn.setOnClickListener {
            PreviewManager.create<SubsamplingScaleImageView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mShowPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<SubsamplingScaleImageView, String>(mScaleSwitch.isChecked){
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
                                        GlobalScope.runOnMain {
                                            view.setImage(ImageSource.resource(R.drawable.ic_launcher))
                                        }
                                        return false
                                    }

                                    override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        GlobalScope.runOnMain {
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
                                if (mClickCloseSwitch.isChecked) {
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
                    .build(IMG_URLS)
                    .open(getContext())
        }

        mLongBtn.setOnClickListener {
            if (mScaleSwitch.isChecked){
                mScaleSwitch.isChecked = false
                toastShort(R.string.preview_unsupport_scale)
            }
            PreviewManager.create<LongImageView, String>()
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mShowPagerSwitch.isChecked)
                    .setImageView(object : AbsImageView<LongImageView, String>(mScaleSwitch.isChecked){
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
                                        GlobalScope.runOnMain {
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
                                if (mClickCloseSwitch.isChecked) {
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
                    .build(IMG_URLS)
                    .open(getContext())
        }
    }

    override fun initData() {
        super.initData()
        mPositionTv.text = (mPosition + 1).toString()
        showStatusCompleted()
    }
}