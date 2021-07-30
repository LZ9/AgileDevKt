package com.lodz.android.agiledevkt.modules.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.agiledevkt.databinding.FragmentTestBinding
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    private val mBinding: FragmentTestBinding by bindingLayout(FragmentTestBinding::inflate)

    private var mContent = ""

    override fun startCreate() {
        super.startCreate()
        val arg = arguments
        if (arg != null){
            mContent = arg.getString(EXTRA_CONTENT, "")
        }
    }

    override fun getAbsViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun initData(view: View) {
        super.initData(view)
        mBinding.contentTv.text = mContent
    }

}