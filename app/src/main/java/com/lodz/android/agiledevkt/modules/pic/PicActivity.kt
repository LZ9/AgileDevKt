package com.lodz.android.agiledevkt.modules.pic

import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityPicBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.pic.ninegrid.NineGridActivity
import com.lodz.android.agiledevkt.modules.pic.picker.PicPickerTestActivity
import com.lodz.android.agiledevkt.modules.pic.preview.PicPreviewTestActivity
import com.lodz.android.agiledevkt.modules.pic.take.TakePhotoTestActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 文件选择测试类
 * Created by zhouL on 2018/12/13.
 */
class PicActivity : BaseActivity() {

    private val mBinding: ActivityPicBinding by bindingLayout(ActivityPicBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun setListeners() {
        super.setListeners()
        // 文件选择按钮
        mBinding.pickBtn.setOnClickListener {
            PicPickerTestActivity.start(getContext())
        }

        // 预览按钮
        mBinding.previewBtn.setOnClickListener {
            PicPreviewTestActivity.start(getContext())
        }

        // 拍照按钮
        mBinding.takePhotoBtn.setOnClickListener {
            TakePhotoTestActivity.start(getContext())
        }

        // 获取九宫格数据按钮
        mBinding.nineGridBtn.setOnClickListener {
            NineGridActivity.start(getContext())
        }
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}