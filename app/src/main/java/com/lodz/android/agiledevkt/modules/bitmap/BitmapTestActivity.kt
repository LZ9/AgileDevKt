package com.lodz.android.agiledevkt.modules.bitmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.widget.custom.LongImageView
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.GlobalScope
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Bitmap图片测试类
 * Created by zhouL on 2018/7/2.
 */
class BitmapTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BitmapTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 原始图片 */
    private val mSrcImg by bindView<ImageView>(R.id.src_img)

    /** Base64编码的图片 */
    private val mBase64Img by bindView<ImageView>(R.id.base64_img)

    /** drawable图片 */
    private val mDrawableImg by bindView<ImageView>(R.id.drawable_img)

    /** drawable转bitmap */
    private val mDrawableBitmapImg by bindView<ImageView>(R.id.drawable_bitmap_img)

    /** view图片 */
    private val mViewImg by bindView<ImageView>(R.id.view_img)

    /** view转bitmap */
    private val mViewBitmapImg by bindView<ImageView>(R.id.view_bitmap_img)

    /** 合并图片 */
    private val mCombineImg by bindView<ImageView>(R.id.combine_img)

    /** 灰度图 */
    private val mGreyImg by bindView<ImageView>(R.id.grey_img)

    /** 圆角图 */
    private val mRoundedCornerImg by bindView<ImageView>(R.id.rounded_corner_img)

    /** 圆形图 */
    private val mRoundedImg by bindView<ImageView>(R.id.rounded_img)

    /** 倒影图 */
    private val mReflectionImg by bindView<ImageView>(R.id.reflection_img)

    /** 旋转90度图片 */
    private val mRotateImg by bindView<ImageView>(R.id.rotate_img)

    /** 水平翻转 */
    private val mReverseHorizontalImg by bindView<ImageView>(R.id.reverse_horizontal_img)

    /** 垂直翻转 */
    private val mReverseVerticalImg by bindView<ImageView>(R.id.reverse_vertical_img)

    /** 色调调整图片 */
    private val mToneImg by bindView<ImageView>(R.id.tone_img)
    /** 色调调整栏 */
    private val mToneSeekBar by bindView<SeekBar>(R.id.tone_seekbar)

    /** 饱和度调整图片 */
    private val mSaturationImg by bindView<ImageView>(R.id.saturation_img)
    /** 饱和度调整栏 */
    private val mSaturationSeekBar by bindView<SeekBar>(R.id.saturation_seekbar)

    /** 亮度值调整图片 */
    private val mLuminanceImg by bindView<ImageView>(R.id.luminance_img)
    /** 亮度值调整栏 */
    private val mLuminanceSeekBar by bindView<SeekBar>(R.id.luminance_seekbar)

    /** 色相值调整图片 */
    private val mHueImg by bindView<ImageView>(R.id.hue_img)
    /** 色相值调整栏 */
    private val mHueSeekBar by bindView<SeekBar>(R.id.hue_seekbar)

    /** 怀旧效果图片 */
    private val mNostalgicImg by bindView<ImageView>(R.id.nostalgic_img)

    /** 光照强度调整图片 */
    private val mSunshineImg by bindView<ImageView>(R.id.sunshine_img)
    /** 光照强度调整栏 */
    private val mSunshineSeekBar by bindView<SeekBar>(R.id.sunshine_seekbar)

    /** 底片效果图片 */
    private val mFilmImg by bindView<ImageView>(R.id.film_img)

    /** 锐化效果图片 */
    private val mSharpenImg by bindView<ImageView>(R.id.sharpen_img)

    /** 浮雕效果图片 */
    private val mEmbossImg by bindView<ImageView>(R.id.emboss_img)

    /** 长图 */
    private val mLargeImg by bindView<ImageView>(R.id.large_img)

    /** 长图 */
    private val mLongImg by bindView<LongImageView>(R.id.long_img)

    override fun getLayoutId() = R.layout.activity_bitmap_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 色调调整栏
        mToneSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                var newProgress = progress + 1
                if (progress < 1) {
                    newProgress = 1
                }
                if (progress > 23) {
                    newProgress = 23
                }
                showToneBitmap(newProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // 饱和度调整栏
        mSaturationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                var newProgress: Float = progress / 100.0f
                if (newProgress < 0f) {
                    newProgress = 0f
                }
                if (newProgress > 2f) {
                    newProgress = 2f
                }
                showSaturationBitmap(newProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // 亮度值调整栏
        mLuminanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                var newProgress: Float = progress / 100.0f
                if (newProgress < 0f) {
                    newProgress = 0f
                }
                if (newProgress > 2f) {
                    newProgress = 2f
                }
                showLuminanceBitmap(newProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // 色相值调整栏
        mHueSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                var newProgress: Float = progress / 100.0f
                if (newProgress < 0f) {
                    newProgress = 0f
                }
                if (newProgress > 2f) {
                    newProgress = 2f
                }
                showHueBitmap(newProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // 光照强度调整栏
        mSunshineSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                var newProgress = progress
                if (newProgress < 0) {
                    newProgress = 0
                }
                if (newProgress > 100) {
                    newProgress = 100
                }
                showSunshineBitmap(newProgress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun initData() {
        super.initData()
        showSrcImg()
        showSrcBase64()
        showDrawableAndBitmap()
        showViewBitmap()
        showCombineBitmap()
        showGreyBitmap()
        showRoundedCornerBitmap()
        showRoundedBitmap()
        showReflectionBitmap()
        showRotateBitmap()
        showReverseHorizontalBitmap()
        showReverseVerticalBitmap()
        showToneBitmap(mToneSeekBar.progress)
        showSaturationBitmap(mToneSeekBar.progress / 100f)
        showLuminanceBitmap(mLuminanceSeekBar.progress / 100f)
        showHueBitmap(mHueSeekBar.progress / 100f)
        showNostalgicBitmap()
        showSunshineBitmap(mSunshineSeekBar.progress.toFloat())
        showFilmBitmap()
        showSharpenBitmap()
        showEmbossBitmap()
        showLargeBitmap()
        showLongBitmap()
        showStatusCompleted()
    }

    /** 显示原始图片 */
    private fun showSrcImg() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapFactory.decodeResource(resources, id)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mSrcImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mSrcImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示原始图片的base64 */
    private fun showSrcBase64() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    val bitmap = BitmapFactory.decodeResource(resources, id)
                    val base64 = BitmapUtils.bitmapToBase64(bitmap, 10)
                    PrintLog.dS("testtag", "base64 : $base64")
                    base64
                }
                .map { base64 ->
                    BitmapUtils.base64ToBitmap(base64) ?: throw KotlinNullPointerException("bitmap is null")
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mBase64Img.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        toastShort(e.message ?: "转换失败")
                    }
                })
    }

    /** 显示Drawable转Bitmap */
    private fun showDrawableAndBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    getDrawableCompat(id)
                }
                .map { drawable ->
                    mDrawableImg.setImageDrawable(drawable)
                    drawable ?: throw KotlinNullPointerException("drawable is null")
                    BitmapUtils.drawableToBitmap(drawable, 331, 162) ?: throw KotlinNullPointerException("bitmap is null")
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mDrawableBitmapImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mDrawableBitmapImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示View转Bitmap */
    private fun showViewBitmap() {
        Observable.just(mViewImg)
                .delay(500, TimeUnit.MILLISECONDS)
                .map { view ->
                    BitmapUtils.viewToBitmap(view) ?: throw KotlinNullPointerException("bitmap is null")
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mViewBitmapImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mViewBitmapImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示合并图片Bitmap */
    private fun showCombineBitmap() {
        Observable.just("")
                .map { any ->
                    val fg = BitmapFactory.decodeResource(resources, R.drawable.ic_regret)
                    val bgd = BitmapFactory.decodeResource(resources, R.drawable.bg_pokemon)
                    BitmapUtils.combineBitmap(fg, bgd, BitmapUtils.CENTER)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mCombineImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mCombineImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示灰度图Bitmap */
    private fun showGreyBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createGreyBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mGreyImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mGreyImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示圆角图Bitmap */
    private fun showRoundedCornerBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createRoundedCornerBitmap(BitmapFactory.decodeResource(resources, id), 12f)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mRoundedCornerImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mRoundedCornerImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示圆形图Bitmap */
    private fun showRoundedBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createRoundBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mRoundedImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mRoundedImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示倒影图Bitmap */
    private fun showReflectionBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createReflectionBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mReflectionImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mReflectionImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示旋转90度Bitmap */
    private fun showRotateBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.rotateBitmap(BitmapFactory.decodeResource(resources, id), 45)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mRotateImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mRotateImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示水平翻转Bitmap */
    private fun showReverseHorizontalBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.reverseBitmapHorizontal(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mReverseHorizontalImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mReverseHorizontalImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示垂直翻转Bitmap */
    private fun showReverseVerticalBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.reverseBitmapVertical(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mReverseVerticalImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mReverseVerticalImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示色调调整Bitmap */
    private fun showToneBitmap(progress: Int) {
        Observable.just(progress)
                .map { value ->
                    BitmapUtils.setBitmapTone(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), value)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mToneImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mToneImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示饱和度调整Bitmap */
    private fun showSaturationBitmap(progress: Float) {
        Observable.just(progress)
                .map { value ->
                    BitmapUtils.setBitmapSaturation(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mSaturationImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mSaturationImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示亮度值调整Bitmap */
    private fun showLuminanceBitmap(progress: Float) {
        Observable.just(progress)
                .map { value ->
                    BitmapUtils.setBitmapLuminance(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mLuminanceImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mLuminanceImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示色相值调整Bitmap */
    private fun showHueBitmap(progress: Float) {
        Observable.just(progress)
                .map { value ->
                    BitmapUtils.setBitmapHue(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mHueImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mHueImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }


    /** 显示怀旧效果Bitmap */
    private fun showNostalgicBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createNostalgicBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mNostalgicImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mNostalgicImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示光照强度Bitmap */
    private fun showSunshineBitmap(progress: Float) {
        Observable.just(progress)
                .map { value ->
                    BitmapUtils.createSunshineBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), 150, 150, progress)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mSunshineImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mSunshineImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示底片效果Bitmap */
    private fun showFilmBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createFilmBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mFilmImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mFilmImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示锐化效果Bitmap */
    private fun showSharpenBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createSharpenBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mSharpenImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mSharpenImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示浮雕效果Bitmap */
    private fun showEmbossBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map { id ->
                    BitmapUtils.createEmbossBitmap(BitmapFactory.decodeResource(resources, id))
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mEmbossImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mEmbossImg.setImageResource(R.drawable.ic_launcher)
                    }
                })
    }

    /** 显示长图 */
    private fun showLargeBitmap() {
        val url = "https://wx1.sinaimg.cn/mw690/71696c12ly1g3qj9n8qo8j20m8209wr2.jpg"
        ImageLoader.create(this).loadUrl(url)
            .download(object :RequestListener<File>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                    GlobalScope.runOnMain {
                        // 监听器回调可能不在主线程
                        mLargeImg.setImageResource(R.drawable.ic_launcher)
                    }
                    return false
                }

                override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    GlobalScope.runOnMain {
                        if (resource == null) {
                            mLargeImg.setImageResource(R.drawable.ic_launcher)
                        } else {
                            Observable.just(resource)
                                .map { file ->
                                    return@map BitmapUtils.createLongLargeBitmap(getContext(), BitmapFactory.decodeFile(file.absolutePath))

                                }.compose(RxUtils.ioToMainObservable())
                                .subscribe(object : BaseObserver<Bitmap>() {
                                    override fun onBaseNext(any: Bitmap) {
                                        mLargeImg.setImageBitmap(any)
                                    }

                                    override fun onBaseError(e: Throwable) {
                                        mLargeImg.setImageResource(R.drawable.ic_launcher)
                                    }

                                })
                        }
                    }
                    return false
                }
            })
    }

    /** 显示长图 */
    private fun showLongBitmap() {
        mLongImg.setImageRes(R.drawable.ic_large)
    }
}
