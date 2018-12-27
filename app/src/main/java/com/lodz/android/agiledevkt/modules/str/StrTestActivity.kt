package com.lodz.android.agiledevkt.modules.str

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.StringUtils
import com.lodz.android.pandora.base.activity.BaseActivity

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
    private val mOriginalTv by bindView<TextView>(R.id.original_tv)
    /** UTF-8编码 */
    private val mEncodeTv by bindView<TextView>(R.id.encode_tv)
    /** UTF-8解码 */
    private val mDecodeTv by bindView<TextView>(R.id.decode_tv)
    /** 分隔符字符串转数组 */
    private val mSeparatorToListTv by bindView<TextView>(R.id.separator_to_list_tv)
    /** 数组转分隔符字符串 */
    private val mListToSeparatorTv by bindView<TextView>(R.id.list_to_separator_tv)

    /** 原始字符 */
    private val mOriginalStr = "测试asdqw123@#!$%<>?}*/-{}:"
    /** 编码字符 */
    private var mEncodeStr = ""
    /** 原始分隔符字符 */
    private val mOriginalSeparatorStr = "asd,wqe,wrdf,cx,123,^&,"
    /** 原始数组 */
    private val mOriginalList = listOf("dwe", "123", "&*%&", "@[]<48", "")

    override fun getLayoutId() = R.layout.activity_str_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        mOriginalTv.text = (getString(R.string.str_original) + mOriginalStr)

        mEncodeStr = StringUtils.encodeUtf8(mOriginalStr)
        mEncodeTv.text = (getString(R.string.str_encode) + mEncodeStr)
        mDecodeTv.text = (getString(R.string.str_decode) + StringUtils.decodeUtf8(mEncodeStr))

        val list = StringUtils.getListBySeparator(mOriginalSeparatorStr, ",")
        mSeparatorToListTv.text = (getString(R.string.str_separator_to_list) + list.toString())

        mListToSeparatorTv.text = (getString(R.string.str_list_to_separator) + StringUtils.getStringBySeparator(mOriginalList, "/"))

        showStatusCompleted()
    }
}