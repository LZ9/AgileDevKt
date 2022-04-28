package com.lodz.android.agiledevkt.modules.str

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityStrTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 字符测试类
 * Created by zhouL on 2018/7/24.
 */
class StrTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, StrTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 原始字符 */
    private val mOriginalStr = "测试asdqw123@#!$%<>?}*/-{}:"
    /** 编码字符 */
    private var mEncodeStr = ""
    /** 原始分隔符字符 */
    private val mOriginalSeparatorStr = "asd,wqe,wrdf,cx,123,^&,"
    /** 原始数组 */
    private val mOriginalList = listOf("dwe", "123", "&*%&", "@[]<48", "")


    private val mBinding: ActivityStrTestBinding by bindingLayout(ActivityStrTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        mBinding.originalTv.text = getString(R.string.str_original).append(mOriginalStr)// 原始字符

        mEncodeStr = mOriginalStr.encodeUtf8()
        mBinding.encodeTv.text = getString(R.string.str_encode).append(mEncodeStr)// UTF-8编码
        mBinding.decodeTv.text = getString(R.string.str_decode).append(mEncodeStr.decodeUtf8())// UTF-8解码

        mBinding.originalListStrTv.text = getString(R.string.str_original).append(mOriginalSeparatorStr)
        mBinding.separatorToListTv.text = getString(R.string.str_separator_to_list)
            .append(mOriginalSeparatorStr.getListBySeparator(","))// 分隔符字符串转数组
        mBinding.originalListTv.text = getString(R.string.str_original_list).append(mOriginalList)
        mBinding.listToSeparatorTv.text = getString(R.string.str_list_to_separator)
            .append(mOriginalList.getStringBySeparator("/"))// 数组转分隔符字符串
        showStatusCompleted()
    }
}