package com.lodz.android.agiledevkt.modules.snackbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.corekt.utils.toastShort

/**
 * Snackbar测试类
 * Created by zhouL on 2018/9/11.
 */
class SnackbarTestActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SnackbarTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 根布局 */
    @BindView(R.id.root_view)
    lateinit var mRootView: CoordinatorLayout
    /** 标题栏 */
    @BindView(R.id.title_bar_layout)
    lateinit var mTitleBarLayout: TitleBarLayout
    /** 显示短的snackbar按钮 */
    @BindView(R.id.short_btn)
    lateinit var mShortBtn: FloatingActionButton
    /** 关注按钮 */
    @BindView(R.id.attention_btn)
    lateinit var mAttentionBtn: Button
    /** 自定义图片按钮 */
    @BindView(R.id.custom_img_btn)
    lateinit var mCustomImgBtn: Button
    /** 自定义布局按钮 */
    @BindView(R.id.custom_layout_btn)
    lateinit var mCustomLayoutBtn: Button

    override fun getAbsLayoutId() = R.layout.activity_snackbar_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        mTitleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun setListeners() {
        super.setListeners()

        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mShortBtn.setOnClickListener {
            SnackbarUtils.createShort(mRootView, R.string.snackbar_sign_title)
                    .setTextColor(Color.WHITE)
                    .setTextSize(17)
                    .setBackgroundColor(getColorCompat(R.color.color_00a0e9))
                    .addLeftImage(R.drawable.ic_launcher, dp2px(5).toInt())
                    .getSnackbar()
                    .setActionTextColor(getColorCompat(R.color.color_ffa630))
                    .setAction(getString(R.string.snackbar_sign), { view ->
                        toastShort(R.string.snackbar_sign)
                    })
                    .show()

        }

        // 关注按钮
        mAttentionBtn.setOnClickListener {
            SnackbarUtils.createLong(mRootView, R.string.snackbar_attention_title)
                    .setTextColor(Color.WHITE)
                    .setTextSize(15)
                    .setBackgroundColor(getColorCompat(R.color.color_ea5e5e))
                    .addRightImage(R.drawable.ic_launcher, dp2px(5).toInt())
                    .getSnackbar()
                    .setActionTextColor(getColorCompat(R.color.color_ffa630))
                    .setAction(getString(R.string.snackbar_cancel_attention), { view ->
                        toastShort(R.string.snackbar_cancel_attention)
                    })
                    .show()
        }

        // 自定义图片按钮
        mCustomImgBtn.setOnClickListener {
            val imageView = ImageView(getContext())
            imageView.setImageResource(R.drawable.ic_regret)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            SnackbarUtils.createLong(mRootView, R.string.snackbar_custom_img)
                    .setBackgroundColor(Color.WHITE)
                    .replaceLayoutView(imageView)
                    .show()
        }

        // 自定义布局按钮
        mCustomLayoutBtn.setOnClickListener {
            val snackbar = SnackbarUtils.createLong(mRootView, R.string.snackbar_custom_layout)
                    .setBackgroundColor(Color.WHITE)
                    .replaceLayoutView(R.layout.snackbar_custom)
                    .getSnackbar()
            val detailBtn = snackbar.view.findViewById<Button>(R.id.detail_btn)
            detailBtn.setOnClickListener { view ->
                toastShort(R.string.snackbar_detail)
                snackbar.dismiss()
            }
            snackbar.show()
        }

    }


}