package com.lodz.android.agiledevkt.modules.bitmap

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityBitmapTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.MainScope
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

    private val mBinding: ActivityBitmapTestBinding by bindingLayout(ActivityBitmapTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

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
        mBinding.toneSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        mBinding.saturationSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        mBinding.luminanceSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        mBinding.hueSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        mBinding.sunshineSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        showToneBitmap(mBinding.toneSeekbar.progress)
        showSaturationBitmap(mBinding.toneSeekbar.progress / 100f)
        showLuminanceBitmap(mBinding.luminanceSeekbar.progress / 100f)
        showHueBitmap(mBinding.hueSeekbar.progress / 100f)
        showNostalgicBitmap()
        showSunshineBitmap(mBinding.sunshineSeekbar.progress.toFloat())
        showFilmBitmap()
        showSharpenBitmap()
        showEmbossBitmap()
        downloadLargeBitmap()
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
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.srcImg.setImageBitmap(bitmap)
                    },
                    error = {
                        mBinding.srcImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示原始图片的base64 */
    private fun showSrcBase64() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                val bitmap = BitmapFactory.decodeResource(resources, id)
                BitmapUtils.bitmapToBase64(bitmap, 10)
            }
            .map { base64 ->
                BitmapUtils.base64ToBitmap(base64) ?: throw KotlinNullPointerException("bitmap is null")
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.base64Img.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.base64Img.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示Drawable转Bitmap */
    private fun showDrawableAndBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                getDrawableCompat(id)
            }
            .map { drawable ->
                mBinding.drawableImg.setImageDrawable(drawable)
                drawable ?: throw KotlinNullPointerException("drawable is null")
                BitmapUtils.drawableToBitmap(drawable, 331, 162) ?: throw KotlinNullPointerException("bitmap is null")
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.drawableBitmapImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.drawableBitmapImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示View转Bitmap */
    private fun showViewBitmap() {
        Observable.just(mBinding.viewImg)
            .delay(500, TimeUnit.MILLISECONDS)
            .map { view ->
                BitmapUtils.viewToBitmap(view) ?: throw KotlinNullPointerException("bitmap is null")
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.viewBitmapImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.viewBitmapImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示合并图片Bitmap */
    private fun showCombineBitmap() {
        Observable.just("")
            .map {
                val fg = BitmapFactory.decodeResource(resources, R.drawable.ic_regret)//前景
                val bgd = BitmapFactory.decodeResource(resources, R.drawable.bg_pokemon)//背景
                BitmapUtils.combineBitmap(fg, bgd, BitmapUtils.CENTER)//居中合并
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.combineImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.combineImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示灰度图Bitmap */
    private fun showGreyBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createGreyBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.greyImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.greyImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示圆角图Bitmap */
    private fun showRoundedCornerBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createRoundedCornerBitmap(BitmapFactory.decodeResource(resources, id), 12f)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.roundedCornerImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.roundedCornerImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示圆形图Bitmap */
    private fun showRoundedBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createRoundBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.roundedImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.roundedImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示倒影图Bitmap */
    private fun showReflectionBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createReflectionBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.reflectionImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.reflectionImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示旋转45度Bitmap */
    private fun showRotateBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.rotateBitmap(BitmapFactory.decodeResource(resources, id), 45f)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.rotateImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.rotateImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示水平翻转Bitmap */
    private fun showReverseHorizontalBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.reverseBitmapHorizontal(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.reverseHorizontalImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.reverseHorizontalImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示垂直翻转Bitmap */
    private fun showReverseVerticalBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.reverseBitmapVertical(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.reverseVerticalImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.reverseVerticalImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示色调调整Bitmap */
    private fun showToneBitmap(progress: Int) {
        Observable.just(progress)
            .map { value ->
                BitmapUtils.setBitmapTone(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), value)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.toneImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.toneImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示饱和度调整Bitmap */
    private fun showSaturationBitmap(progress: Float) {
        Observable.just(progress)
            .map { value ->
                BitmapUtils.setBitmapSaturation(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), value)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.saturationImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.saturationImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示亮度值调整Bitmap */
    private fun showLuminanceBitmap(progress: Float) {
        Observable.just(progress)
            .map { value ->
                BitmapUtils.setBitmapLuminance(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), value)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.luminanceImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.luminanceImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示色相值调整Bitmap */
    private fun showHueBitmap(progress: Float) {
        Observable.just(progress)
            .map { value ->
                BitmapUtils.setBitmapHue(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), value)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.hueImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.hueImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }


    /** 显示怀旧效果Bitmap */
    private fun showNostalgicBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createNostalgicBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.nostalgicImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.nostalgicImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示光照强度Bitmap */
    private fun showSunshineBitmap(progress: Float) {
        Observable.just(progress)
            .map { value ->
                BitmapUtils.createSunshineBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_regret), 150, 150, value)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.sunshineImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.sunshineImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示底片效果Bitmap */
    private fun showFilmBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createFilmBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.filmImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.filmImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示锐化效果Bitmap */
    private fun showSharpenBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createSharpenBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.sharpenImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.sharpenImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示浮雕效果Bitmap */
    private fun showEmbossBitmap() {
        Observable.just(R.drawable.ic_regret)
            .map { id ->
                BitmapUtils.createEmbossBitmap(BitmapFactory.decodeResource(resources, id))
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.embossImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.embossImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 下载长图 */
    private fun downloadLargeBitmap() {
        val url = "https://wx1.sinaimg.cn/mw690/71696c12ly1g3qj9n8qo8j20m8209wr2.jpg"
        ImageLoader.create(this).loadUrl(url)
            .download(object :RequestListener<File>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                    runOnMain {
                        // 监听器回调可能不在主线程
                        mBinding.largeImg.setImageResource(R.drawable.ic_launcher)
                    }
                    return false
                }

                override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    runOnMain {
                        if (resource != null) {
                            showLargeBitmap(resource)
                            return@runOnMain
                        }
                        mBinding.largeImg.setImageResource(R.drawable.ic_launcher)
                    }
                    return false
                }
            })
    }

    /** 显示长图 */
    private fun showLargeBitmap(resource: File) {
        Observable.just(resource)
            .map { file ->
                return@map BitmapUtils.createLongLargeBitmap(getContext(), BitmapFactory.decodeFile(file.absolutePath))
            }.compose(RxUtils.ioToMainObservable())
            .subscribe(
                BaseObserver.action(
                    next = { bitmap ->
                        mBinding.largeImg.setImageBitmap(bitmap)
                    },
                    error = { e ->
                        mBinding.largeImg.setImageResource(R.drawable.ic_launcher)
                    })
            )
    }

    /** 显示长图 */
    private fun showLongBitmap() {
        mBinding.longImg.setImageRes(R.drawable.ic_large)
    }
}
