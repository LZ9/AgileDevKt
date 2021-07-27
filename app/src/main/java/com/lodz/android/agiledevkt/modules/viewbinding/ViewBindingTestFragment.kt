package com.lodz.android.agiledevkt.modules.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.databinding.FragmentViewBindingTestBinding
import com.lodz.android.pandora.base.fragment.LazyFragment

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/19
 */
class ViewBindingTestFragment : LazyFragment() {

    companion object {
        private const val EXTRA_NAME = "extra_name"

        fun newInstance(name: String): ViewBindingTestFragment {
            val fragment = ViewBindingTestFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mBinding: FragmentViewBindingTestBinding? = null

    override fun getAbsViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentViewBindingTestBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        initRecyclerView()
    }

    override fun initData(view: View) {
        super.initData(view)
        val name = arguments?.getString(EXTRA_NAME) ?: "无"
        mBinding?.textTv?.text = name
    }

    private fun initRecyclerView() {
        val adapter = ViewBindingAdapter(requireContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mBinding?.recyclerView?.layoutManager = layoutManager
        mBinding?.recyclerView?.setHasFixedSize(true)
        mBinding?.recyclerView?.adapter = adapter
        adapter.setData(getTestList())
    }

    private fun getTestList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..99) {
            list.add((i + 1).toString())
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}