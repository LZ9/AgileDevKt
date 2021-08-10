package com.lodz.android.agiledevkt.modules.rv.binder.second

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.databinding.RvBinderSecondBinding
import com.lodz.android.pandora.widget.rv.binder.RecyclerBinder

/**
 * Created by zhouL on 2018/12/11.
 */
class SecondBinder(context: Context, binderType: Int) : RecyclerBinder<List<NationBean>>(context, binderType) {

    private var mData: List<NationBean>? = null
    private var mListener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        DataViewHolder(getViewBindingLayout(RvBinderSecondBinding::inflate, parent))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val list = mData
        if (list.isNullOrEmpty() || holder !is DataViewHolder) {
            return
        }
        holder.setData(list)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setData(list: List<NationBean>) {
        mData = list
    }

    override fun getData(position: Int): List<NationBean>? = mData

    override fun getCount(): Int = 1

    private inner class DataViewHolder(private val binding: RvBinderSecondBinding) : RecyclerView.ViewHolder(binding.root) {

        private var mAdapter: SecondBinderAdapter? = null

        fun setData(list: List<NationBean>) {
            if (mAdapter == null) {
                val layoutManager = GridLayoutManager(getContext(), 2)
                layoutManager.orientation = RecyclerView.VERTICAL
                mAdapter = SecondBinderAdapter(getContext())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.setHasFixedSize(true)
                binding.recyclerView.adapter = mAdapter
                mAdapter?.setOnItemClickListener { viewHolder, item, position ->
                    mListener?.onClick(item)
                }
            }
            mAdapter?.setData(list.toMutableList())
            mAdapter?.notifyDataSetChanged()
        }
    }

    fun interface Listener {
        fun onClick(item: NationBean)
    }
}