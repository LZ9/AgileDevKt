package com.lodz.android.agiledevkt.ui.bitmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.ui.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.corekt.utils.toastShort
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Bitmap图片测试类
 * Created by zhouL on 2018/7/2.
 */
class BitmapTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
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


    override fun getLayoutId() = R.layout.activity_bitmap_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showSrcImg()
        showSrcBase64()
        showStatusCompleted()
    }

    /** 显示原始图片 */
    private fun showSrcImg() {
        mSrcImg.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_regret))
    }

    /** 显示原始图片的base64 */
    private fun showSrcBase64() {
        Observable.just("")
                .map(object : Function<String, String> {
                    override fun apply(str: String): String {
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_regret)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bitmap ->
                    mBase64Img.setImageBitmap(bitmap)
                }, { e ->
                    toastShort(e.message ?: "转换失败")
                })
    }
}