package com.lodz.android.agiledevkt.modules.rv.binder.first

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.widget.rv.binder.RecyclerBinder
import com.lodz.android.pandora.widget.rv.horizontal.HorRecyclerView

/**
 * Created by zhouL on 2018/12/10.
 */
class FirstBinder(context: Context, binderType: Int) : RecyclerBinder<List<NationBean>>(context, binderType) {

    private var mData: List<NationBean>? = null
    private var mListener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_binder_first))


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

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 水平RV */
        val horRv by bindView<HorRecyclerView>(R.id.hor_recycler_view)

        private var mAdapter: FirstBinderAdapter? = null

        fun setData(list: List<NationBean>) {
            if (mAdapter == null) {
                val layoutManager = LinearLayoutManager(getContext())
                layoutManager.orientation = RecyclerView.HORIZONTAL
                mAdapter = FirstBinderAdapter(getContext())
                horRv.layoutManager = layoutManager
                horRv.setHasFixedSize(true)
                horRv.adapter = mAdapter
                mAdapter!!.setOnItemClickListener { viewHolder, item, position ->
                    if (mListener != null) {
                        mListener!!.onClick(item)
                    }
                }
            }
            mAdapter!!.setData(list.toMutableList())
            mAdapter!!.notifyDataSetChanged()
        }
    }

    interface Listener {
        fun onClick(item: NationBean)
    }
}