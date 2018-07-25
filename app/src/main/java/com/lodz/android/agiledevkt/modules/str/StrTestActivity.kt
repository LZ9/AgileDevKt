package com.lodz.android.agiledevkt.modules.str

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.StringUtils

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
    @BindView(R.id.original_tv)
    lateinit var mOriginalTv: TextView
    /** UTF-8编码 */
    @BindView(R.id.encode_tv)
    lateinit var mEncodeTv: TextView
    /** UTF-8解码 */
    @BindView(R.id.decode_tv)
    lateinit var mDecodeTv: TextView
    /** 分隔符字符串转数组 */
    @BindView(R.id.separator_to_list_tv)
    lateinit var mSeparatorToListTv: TextView
    /** 数组转分隔符字符串 */
    @BindView(R.id.list_to_separator_tv)
    lateinit var mListToSeparatorTv: TextView

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
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
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