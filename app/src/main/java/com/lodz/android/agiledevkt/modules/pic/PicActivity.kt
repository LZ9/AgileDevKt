package com.lodz.android.agiledevkt.modules.pic

import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.pic.ninegrid.NineGridActivity
import com.lodz.android.agiledevkt.modules.pic.picker.PicPickerTestActivity
import com.lodz.android.agiledevkt.modules.pic.preview.PicPreviewTestActivity
import com.lodz.android.agiledevkt.modules.pic.take.TakePhotoTestActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.anko.bindView

/**
 * 图片测试类
 * Created by zhouL on 2018/12/13.
 */
class PicActivity : BaseActivity() {

    /** 图片选择按钮 */
    private val mPickBtn by bindView<MaterialButton>(R.id.pick_btn)
    /** 预览按钮 */
    private val mPreviewBtn by bindView<MaterialButton>(R.id.preview_btn)
    /** 拍照按钮 */
    private val mTakePhotoBtn by bindView<MaterialButton>(R.id.take_photo_btn)
    /** 获取九宫格数据按钮 */
    private val mNineGridBtn by bindView<MaterialButton>(R.id.nine_grid_btn)

    override fun getLayoutId(): Int = R.layout.activity_pic

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun setListeners() {
        super.setListeners()
        mPickBtn.setOnClickListener {
            PicPickerTestActivity.start(getContext())
        }

        mPreviewBtn.setOnClickListener {
            PicPreviewTestActivity.start(getContext())
        }

        mTakePhotoBtn.setOnClickListener {
            TakePhotoTestActivity.start(getContext())
        }

        mNineGridBtn.setOnClickListener {
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