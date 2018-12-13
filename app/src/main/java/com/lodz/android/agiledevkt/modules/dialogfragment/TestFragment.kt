package com.lodz.android.agiledevkt.modules.dialogfragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.fragment.LazyFragment
import com.lodz.android.corekt.anko.bindView

/**
 * 弹框内的测试fragment
 * Created by zhouL on 2018/12/12.
 */
class TestFragment : LazyFragment() {

    companion object {

        private const val EXTRA_CONTENT = "extra_content"

        fun newInstance(content: String): TestFragment {
            val fragment = TestFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_CONTENT, content)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mContentTv by bindView<TextView>(R.id.content_tv)

    private var mContent = ""

    override fun startCreate() {
        super.startCreate()
        if (arguments != null){
            mContent = arguments!!.getString(EXTRA_CONTENT, "")
        }
    }

    override fun getAbsLayoutId(): Int = R.layout.fragment_test

    override fun initData(view: View) {
        super.initData(view)
        mContentTv.text = mContent
    }

}