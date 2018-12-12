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
class TestFragment :LazyFragment(){

    private val mContentTv by bindView<TextView>(R.id.content_tv)

    override fun getAbsLayoutId(): Int = R.layout.fragment_test

    override fun findViews(view: View, savedInstanceState: Bundle?) {
    }
}