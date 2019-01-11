package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.photopicker.preview.PreviewManager

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
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135447478&di=90ddcac4604965af5d9bc744237a27aa&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd52a2834349b033b1c4bcdcf1fce36d3d439bde7.jpg")

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
    /** 预览按钮 */
    private val mPreviewBtn by bindView<MaterialButton>(R.id.preview_btn)


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

        mPreviewBtn.setOnClickListener {
            PreviewManager.create<String>()
                    .setScale(mScaleSwitch.isChecked)
                    .setPosition(mPosition)
                    .setBackgroundColor(R.color.black)
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPagerTextColor(R.color.white)
                    .setPagerTextSize(14)
                    .setShowPagerText(mShowPagerSwitch.isChecked)
                    .setOnClickListener { context, source, position, controller ->
                        if (mClickCloseSwitch.isChecked) {
                            controller.close()
                        } else {
                            toastShort(getString(R.string.preview_click_tips, position.toString()))
                        }
                    }
                    .setOnLongClickListener { context, source, position, controller ->
                        toastShort(getString(R.string.preview_long_click_tips, position.toString()))
                    }
                    .setImgLoader { context, source, imageView ->
                        ImageLoader.create(context).loadUrl(source).setFitCenter().into(imageView)
                    }
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