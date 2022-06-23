package com.lodz.android.agiledevkt.modules.rv.binder.third

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.databinding.RvBinderThirdBinding
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.binder.RecyclerBinder
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * Created by zhouL on 2018/12/11.
 */
class ThirdBinder(context: Context, binderType: Int) : RecyclerBinder<NationBean>(context, binderType) {

    private var mData: List<NationBean>? = null
    private var mListener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvBinderThirdBinding::inflate, parent))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getData(position)
        if (bean == null || holder !is DataVBViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataVBViewHolder, bean: NationBean) {
        holder.getVB<RvBinderThirdBinding>().apply {
            ImageLoader.create(getContext())
                .loadUrl(bean.imgUrl)
                .useCircle()
                .setCenterCrop()
                .into(nationImg)
            nationTv.apply {
                text = StringBuilder("${bean.code}-${bean.name}")
                setOnClickListener {
                    mListener?.onClick(bean)
                }
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

    fun interface Listener {
        fun onClick(item: NationBean)
    }

}