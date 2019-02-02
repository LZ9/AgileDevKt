package com.lodz.android.agiledevkt.modules.snackbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.widget.base.TitleBarLayout

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
    private val mRootView by bindView<CoordinatorLayout>(R.id.root_view)
    /** 标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)
    /** 显示短的snackbar按钮 */
    private val mShortBtn by bindView<FloatingActionButton>(R.id.short_btn)
    /** 关注按钮 */
    private val mAttentionBtn by bindView<Button>(R.id.attention_btn)
    /** 自定义图片按钮 */
    private val mCustomImgBtn by bindView<Button>(R.id.custom_img_btn)
    /** 自定义布局按钮 */
    private val mCustomLayoutBtn by bindView<Button>(R.id.custom_layout_btn)

    override fun getAbsLayoutId() = R.layout.activity_snackbar_test

    override fun findViews(savedInstanceState: Bundle?) {
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