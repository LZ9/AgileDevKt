package com.lodz.android.agiledevkt.modules.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.FragmentTestResultBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.toEditable
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 测试Activity
 * @author zhouL
 * @date 2022/4/11
 */
class TestResultAFragment : LazyFragment() {


    companion object {

        private const val EXTRA_CONTENT = "extra_content"

        fun newInstance(content: String): TestResultAFragment {
            val fragment = TestResultAFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_CONTENT, content)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mBinding: FragmentTestResultBinding by bindingLayout(FragmentTestResultBinding::inflate)

    override fun getAbsViewBindingLayout(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = mBinding.root

    private val mContent by lazy { arguments?.getString(EXTRA_CONTENT) ?: "" }

    override fun setListeners(view: View) {
        super.setListeners(view)
        mBinding.resultBtn.setOnClickListener {
            val str = mBinding.inputEdit.text.toString().append(" | ").append(DateUtils.getCurrentFormatString(DateUtils.TYPE_2))
            if (str.isEmpty()) {
                toastShort(R.string.arc_input_hint)
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putString(TestResultAFragment::class.java.name, str)
            setFragmentResult(TestResultAFragment::class.java.name, bundle)
            toastShort("发送给 TestResultBFragment")
        }

        parentFragmentManager.setFragmentResultListener(TestResultBFragment::class.java.name, viewLifecycleOwner) { requestKey, result ->
            val str = result.getString(TestResultBFragment::class.java.name, "null")
            mBinding.contentTv.text = requestKey.append(" -> ").append(str)
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        mBinding.contentTv.text = mContent
        mBinding.inputEdit.text = mContent.toEditable()
    }

    override fun configIsLazyLoad(): Boolean = false
}