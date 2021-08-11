package com.lodz.android.agiledevkt.modules.snackbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySnackbarTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    private val mBinding: ActivitySnackbarTestBinding by bindingLayout(ActivitySnackbarTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        mBinding.titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 显示短的snackbar按钮
        mBinding.shortBtn.setOnClickListener {
            SnackbarUtils.createShort(mBinding.root, R.string.snackbar_sign_title)
                .setTextColor(Color.WHITE)
                .setTextSize(17)
                .setBackgroundColor(getColorCompat(R.color.color_00a0e9))
                .addLeftImage(R.drawable.ic_launcher, dp2px(5))
                .getSnackbar()
                .setActionTextColor(getColorCompat(R.color.color_ffa630))
                .setAction(getString(R.string.snackbar_sign)) { view ->
                    toastShort(R.string.snackbar_sign)
                }
                .show()
        }

        // 关注按钮
        mBinding.attentionBtn.setOnClickListener {
            SnackbarUtils.createLong(mBinding.root, R.string.snackbar_attention_title)
                .setTextColor(Color.WHITE)
                .setTextSize(15)
                .setBackgroundColor(getColorCompat(R.color.color_ea5e5e))
                .addRightImage(R.drawable.ic_launcher, dp2px(5))
                .getSnackbar()
                .setActionTextColor(getColorCompat(R.color.color_ffa630))
                .setAction(getString(R.string.snackbar_cancel_attention)) { view ->
                    toastShort(R.string.snackbar_cancel_attention)
                }
                .show()
        }

        // 自定义图片按钮
        mBinding.customImgBtn.setOnClickListener {
            val imageView = ImageView(getContext())
            imageView.setImageResource(R.drawable.ic_regret)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            SnackbarUtils.createLong(mBinding.root, R.string.snackbar_custom_img)
                .setBackgroundColor(Color.WHITE)
                .replaceLayoutView(imageView)
                .show()
        }

        // 自定义布局按钮
        mBinding.customLayoutBtn.setOnClickListener {
            val snackbar = SnackbarUtils.createLong(mBinding.root, R.string.snackbar_custom_layout)
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