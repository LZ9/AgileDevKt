package com.lodz.android.agiledevkt.modules.fglifecycle.vp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.agiledevkt.databinding.FragmentTestBinding
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 弹框内的测试fragment
 * Created by zhouL on 2018/12/12.
 */
class TestContentFragment : LazyFragment() {

    companion object {

        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_TOP = "extra_top"
        private const val EXTRA_BOTTOM = "extra_bottom"

        fun newInstance(name: String, top: String, bottom: String): TestContentFragment {
            val fragment = TestContentFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            bundle.putString(EXTRA_TOP, top)
            bundle.putString(EXTRA_BOTTOM, bottom)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mBinding: FragmentTestBinding by bindingLayout(FragmentTestBinding::inflate)

    private var mName = ""
    private var mTop = ""
    private var mBottom = ""

    override fun startCreate() {
        super.startCreate()
        val arg = arguments
        if (arg != null){
            mName = arg.getString(EXTRA_NAME, "")
            mTop = arg.getString(EXTRA_TOP, "")
            mBottom = arg.getString(EXTRA_BOTTOM, "")
        }
    }

    override fun getAbsViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun initData(view: View) {
        super.initData(view)
        mBinding.contentTv.text = mBottom
        PrintLog.i("fgtag", "TestContentFragment $mName/$mTop/$mBottom -> showStatusCompleted")
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        PrintLog.i("fgtag", "TestContentFragment $mName/$mTop/$mBottom -> onFragmentResume")
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        PrintLog.i("fgtag", "TestContentFragment $mName/$mTop/$mBottom -> onFragmentPause")
    }


}