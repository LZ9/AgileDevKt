package com.lodz.android.agiledevkt.modules.rv.binder.first

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.databinding.RvBinderFirstBinding
import com.lodz.android.pandora.widget.rv.binder.RecyclerBinder

/**
 * Created by zhouL on 2018/12/10.
 */
class FirstBinder(context: Context, binderType: Int) : RecyclerBinder<List<NationBean>>(context, binderType) {

    private var mData: List<NationBean>? = null
    private var mListener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            DataViewHolder(getViewBindingLayout(RvBinderFirstBinding::inflate, parent))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val list = mData
        if (list.isNullOrEmpty() || holder !is DataViewHolder) {
            return
        }
        holder.setData(list)
    }

    override fun getCount(): Int = 1

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setData(list: List<NationBean>) {
        mData = list
    }

    override fun getData(position: Int): List<NationBean>? {
        return mData
    }

    private inner class DataViewHolder(private val binding: RvBinderFirstBinding) : RecyclerView.ViewHolder(binding.root) {

        private var mAdapter: FirstBinderAdapter? = null

        fun setData(list: List<NationBean>) {
            if (mAdapter == null) {
                val layoutManager = LinearLayoutManager(getContext())
                layoutManager.orientation = RecyclerView.HORIZONTAL
                mAdapter = FirstBinderAdapter(getContext())
                binding.horRecyclerView.layoutManager = layoutManager
                binding.horRecyclerView.setHasFixedSize(true)
                binding.horRecyclerView.adapter = mAdapter
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