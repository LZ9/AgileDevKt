package com.lodz.android.agiledevkt.modules.rv.binder.third

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.binder.RecyclerBinder
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

/**
 * Created by zhouL on 2018/12/11.
 */
class ThirdBinder(context: Context, binderType: Int) : RecyclerBinder<NationBean>(context, binderType) {

    private var mData: List<NationBean>? = null
    private var mListener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_binder_third))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getData(position)
        if (bean == null || holder !is DataViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataViewHolder, bean: NationBean) {
        ImageLoader.create(getContext())
                .loadUrl(bean.imgUrl)
                .useCircle()
                .setCenterCrop()
                .into(holder.withView<ImageView>(R.id.nation_img))
        holder.withView<TextView>(R.id.nation_tv).apply {
            text = StringBuilder("${bean.code}-${bean.name}")
            setOnClickListener {
                mListener?.onClick(bean)
            }
        }
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setData(list: List<NationBean>) {
        mData = list
    }

    override fun getData(position: Int): NationBean? {
        try {
            return mData?.get(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun getCount(): Int = mData.getSize()

    interface Listener {
        fun onClick(item: NationBean)
    }

}