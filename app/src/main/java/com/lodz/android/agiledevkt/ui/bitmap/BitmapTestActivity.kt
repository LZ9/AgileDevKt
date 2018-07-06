package com.lodz.android.agiledevkt.ui.bitmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.ui.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.rx.subscribe.observer.BaseObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.corekt.utils.toastShort
import io.reactivex.Observable
import io.reactivex.functions.Function
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
    @BindView(R.id.src_img)
    lateinit var mSrcImg: ImageView

    /** Base64编码的图片 */
    @BindView(R.id.base64_img)
    lateinit var mBase64Img: ImageView

    /** drawable图片 */
    @BindView(R.id.drawable_img)
    lateinit var mDrawableImg: ImageView

    /** drawable转bitmap */
    @BindView(R.id.drawable_bitmap_img)
    lateinit var mDrawableBitmapImg: ImageView

    /** view图片 */
    @BindView(R.id.view_img)
    lateinit var mViewImg: ImageView

    /** view转bitmap */
    @BindView(R.id.view_bitmap_img)
    lateinit var mViewBitmapImg: ImageView

    /** 合并图片 */
    @BindView(R.id.combine_img)
    lateinit var mCombineImg: ImageView

    /** 灰度图 */
    @BindView(R.id.grey_img)
    lateinit var mGreyImg: ImageView

    /** 圆角图 */
    @BindView(R.id.rounded_corner_img)
    lateinit var mRoundedCornerImg: ImageView

    /** 圆形图 */
    @BindView(R.id.rounded_img)
    lateinit var mRoundedImg: ImageView

    /** 倒影图 */
    @BindView(R.id.reflection_img)
    lateinit var mReflectionImg: ImageView

    /** 旋转90度图片 */
    @BindView(R.id.rotate_img)
    lateinit var mRotateImg: ImageView

    /** 水平翻转 */
    @BindView(R.id.reverse_horizontal_img)
    lateinit var mReverseHorizontalImg: ImageView

    /** 垂直翻转 */
    @BindView(R.id.reverse_vertical_img)
    lateinit var mReverseVerticalImg: ImageView

    /** 色调调整图片 */
    @BindView(R.id.tone_img)
    lateinit var mToneImg: ImageView
    /** 色调调整栏 */
    @BindView(R.id.tone_seekbar)
    lateinit var mToneSeekBar: SeekBar

    /** 饱和度调整图片 */
    @BindView(R.id.saturation_img)
    lateinit var mSaturationImg: ImageView
    /** 饱和度调整栏 */
    @BindView(R.id.saturation_seekbar)
    lateinit var mSaturationSeekBar: SeekBar

    /** 亮度值调整图片 */
    @BindView(R.id.luminance_img)
    lateinit var mLuminanceImg: ImageView
    /** 亮度值调整栏 */
    @BindView(R.id.luminance_seekbar)
    lateinit var mLuminanceSeekBar: SeekBar

    /** 色相值调整图片 */
    @BindView(R.id.hue_img)
    lateinit var mHueImg: ImageView
    /** 色相值调整栏 */
    @BindView(R.id.hue_seekbar)
    lateinit var mHueSeekBar: SeekBar

    /** 怀旧效果图片 */
    @BindView(R.id.nostalgic_img)
    lateinit var mNostalgicImg: ImageView

    /** 光照强度调整图片 */
    @BindView(R.id.sunshine_img)
    lateinit var mSunshineImg: ImageView
    /** 光照强度调整栏 */
    @BindView(R.id.sunshine_seekbar)
    lateinit var mSunshineSeekBar: SeekBar

    /** 底片效果图片 */
    @BindView(R.id.film_img)
    lateinit var mFilmImg: ImageView

    /** 锐化效果图片 */
    @BindView(R.id.sharpen_img)
    lateinit var mSharpenImg: ImageView

    /** 浮雕效果图片 */
    @BindView(R.id.emboss_img)
    lateinit var mEmbossImg: ImageView

    override fun getLayoutId() = R.layout.activity_bitmap_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
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
        showStatusCompleted()
    }

    /** 显示原始图片 */
    private fun showSrcImg() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap> {
                    override fun apply(id: Int) = BitmapFactory.decodeResource(resources, id)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mSrcImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mSrcImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })

    }

    /** 显示原始图片的base64 */
    private fun showSrcBase64() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, String> {
                    override fun apply(id: Int): String {
                        val bitmap = BitmapFactory.decodeResource(resources, id)
                        val base64 = BitmapUtils.bitmapToBase64(bitmap, 10)
                        PrintLog.dS("testtag", "base64 : " + base64)
                        return base64
                    }
                })
                .map(object : Function<String, Bitmap> {
                    override fun apply(base64: String): Bitmap {
                        val bitmap = BitmapUtils.base64ToBitmap(base64)
                        if (bitmap == null) {
                            throw KotlinNullPointerException("bitmap is null")
                        }
                        return bitmap
                    }
                })
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
                .map(object : Function<Int, Drawable> {
                    override fun apply(id: Int) = getDrawableCompat(id)
                })
                .map(object : Function<Drawable, Bitmap?> {
                    override fun apply(drawable: Drawable): Bitmap? {
                        mDrawableImg.setImageDrawable(drawable)
                        return BitmapUtils.drawableToBitmap(drawable, 331, 162)
                    }
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mDrawableBitmapImg.setImageBitmap(any)
                        } else {
                            mDrawableBitmapImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }

                    }

                    override fun onBaseError(e: Throwable) {
                        mDrawableBitmapImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示View转Bitmap */
    private fun showViewBitmap() {
        Observable.just(mViewImg)
                .delay(500, TimeUnit.MILLISECONDS)
                .map(object : Function<View, Bitmap?> {
                    override fun apply(view: View) = BitmapUtils.viewToBitmap(view)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mViewBitmapImg.setImageBitmap(any)
                        } else {
                            mViewBitmapImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mViewBitmapImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })

    }

    /** 显示合并图片Bitmap */
    private fun showCombineBitmap() {
        Observable.just("")
                .map(object : Function<String, Bitmap?> {
                    override fun apply(t: String): Bitmap? {
                        val fg = BitmapFactory.decodeResource(resources, R.drawable.ic_regret)
                        val bgd = BitmapFactory.decodeResource(resources, R.drawable.bg_pokemon)
                        return BitmapUtils.combineBitmap(fg, bgd, BitmapUtils.CENTER)
                    }
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mCombineImg.setImageBitmap(any)
                        } else {
                            mCombineImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mCombineImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示灰度图Bitmap */
    private fun showGreyBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createGreyBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mGreyImg.setImageBitmap(any)
                        } else {
                            mGreyImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mGreyImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })

    }

    /** 显示圆角图Bitmap */
    private fun showRoundedCornerBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createRoundedCornerBitmap(BitmapFactory.decodeResource(resources, id), 12f)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mRoundedCornerImg.setImageBitmap(any)
                        } else {
                            mRoundedCornerImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mRoundedCornerImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示圆形图Bitmap */
    private fun showRoundedBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createRoundBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mRoundedImg.setImageBitmap(any)
                        } else {
                            mRoundedImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mRoundedImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示倒影图Bitmap */
    private fun showReflectionBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createReflectionBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mReflectionImg.setImageBitmap(any)
                        } else {
                            mReflectionImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mReflectionImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示旋转90度Bitmap */
    private fun showRotateBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.rotateBitmap(BitmapFactory.decodeResource(resources, id), 45)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mRotateImg.setImageBitmap(any)
                        } else {
                            mRotateImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mRotateImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示水平翻转Bitmap */
    private fun showReverseHorizontalBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.reverseBitmapHorizontal(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mReverseHorizontalImg.setImageBitmap(any)
                        } else {
                            mReverseHorizontalImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mReverseHorizontalImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示垂直翻转Bitmap */
    private fun showReverseVerticalBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.reverseBitmapVertical(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mReverseVerticalImg.setImageBitmap(any)
                        } else {
                            mReverseVerticalImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mReverseVerticalImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示色调调整Bitmap */
    private fun showToneBitmap(progress: Int) {
        Observable.just(progress)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(progress: Int) = BitmapUtils.setBitmapTone(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mToneImg.setImageBitmap(any)
                        } else {
                            mToneImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mToneImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示饱和度调整Bitmap */
    private fun showSaturationBitmap(progress: Float) {
        Observable.just(progress)
                .map(object : Function<Float, Bitmap?> {
                    override fun apply(progress: Float) = BitmapUtils.setBitmapSaturation(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mSaturationImg.setImageBitmap(any)
                        } else {
                            mSaturationImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mSaturationImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示亮度值调整Bitmap */
    private fun showLuminanceBitmap(progress: Float) {
        Observable.just(progress)
                .map(object : Function<Float, Bitmap?> {
                    override fun apply(progress: Float) = BitmapUtils.setBitmapLuminance(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mLuminanceImg.setImageBitmap(any)
                        } else {
                            mLuminanceImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mLuminanceImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示色相值调整Bitmap */
    private fun showHueBitmap(progress: Float) {
        Observable.just(progress)
                .map(object : Function<Float, Bitmap?> {
                    override fun apply(progress: Float) = BitmapUtils.setBitmapHue(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), progress)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mHueImg.setImageBitmap(any)
                        } else {
                            mHueImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mHueImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }


    /** 显示怀旧效果Bitmap */
    private fun showNostalgicBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createNostalgicBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mNostalgicImg.setImageBitmap(any)
                        } else {
                            mNostalgicImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mNostalgicImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示光照强度Bitmap */
    private fun showSunshineBitmap(progress: Float) {
        Observable.just(progress)
                .map(object : Function<Float, Bitmap?> {
                    override fun apply(progress: Float) = BitmapUtils.createSunshineBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), 150, 150, progress)
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mSunshineImg.setImageBitmap(any)
                        } else {
                            mSunshineImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mSunshineImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示底片效果Bitmap */
    private fun showFilmBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createFilmBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mFilmImg.setImageBitmap(any)
                        } else {
                            mFilmImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mFilmImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示锐化效果Bitmap */
    private fun showSharpenBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createSharpenBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mSharpenImg.setImageBitmap(any)
                        } else {
                            mSharpenImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mSharpenImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }

    /** 显示浮雕效果Bitmap */
    private fun showEmbossBitmap() {
        Observable.just(R.drawable.ic_regret)
                .map(object : Function<Int, Bitmap?> {
                    override fun apply(id: Int) = BitmapUtils.createEmbossBitmap(BitmapFactory.decodeResource(resources, id))
                })
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap?>() {
                    override fun onBaseNext(any: Bitmap?) {
                        if (any != null) {
                            mEmbossImg.setImageBitmap(any)
                        } else {
                            mEmbossImg.setImageResource(R.drawable.componentkt_ic_launcher)
                        }
                    }

                    override fun onBaseError(e: Throwable) {
                        mEmbossImg.setImageResource(R.drawable.componentkt_ic_launcher)
                    }
                })
    }
}
