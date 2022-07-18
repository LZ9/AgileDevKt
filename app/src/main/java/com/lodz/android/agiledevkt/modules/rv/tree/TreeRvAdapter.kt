package com.lodz.android.agiledevkt.modules.rv.tree

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lodz.android.agiledevkt.bean.city.AreasBean
import com.lodz.android.agiledevkt.bean.city.CityBean
import com.lodz.android.agiledevkt.bean.city.ProvinceBean
import com.lodz.android.agiledevkt.databinding.RvItemTreeAreasBinding
import com.lodz.android.agiledevkt.databinding.RvItemTreeCityBinding
import com.lodz.android.agiledevkt.databinding.RvItemTreeProvinceBinding
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder
import com.lodz.android.pandora.widget.rv.tree.BaseTreeRvAdapter

/**
 * RV树结构基类
 * @author zhouL
 * @date 2022/7/15
 */
class TreeRvAdapter(context: Context) : BaseTreeRvAdapter<ProvinceBean>(context){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataVBViewHolder {
        if (viewType == ProvinceBean::class.java.hashCode()) {
            return ProvinceViewHolder(getViewBindingLayout(RvItemTreeProvinceBinding::inflate, parent))
        }
        if (viewType == CityBean::class.java.hashCode()) {
            return CityViewHolder(getViewBindingLayout(RvItemTreeCityBinding::inflate, parent))
        }
        return AreasViewHolder(getViewBindingLayout(RvItemTreeAreasBinding::inflate, parent))
    }

    override fun onBind(holder: DataVBViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is ProvinceViewHolder && item is ProvinceBean){
            holder.getVB<RvItemTreeProvinceBinding>().nameTv.text = "${item.provinceId} : ${item.provinceName}"
        }
        if (holder is CityViewHolder && item is CityBean){
            holder.getVB<RvItemTreeCityBinding>().nameTv.text = "${item.cityId} : ${item.cityName}"
        }
        if (holder is AreasViewHolder && item is AreasBean){
            holder.getVB<RvItemTreeAreasBinding>().nameTv.text = "${item.areaId} : ${item.areaName}"
        }
    }

    class ProvinceViewHolder(viewBinding: ViewBinding) : DataVBViewHolder(viewBinding)

    class CityViewHolder(viewBinding: ViewBinding) : DataVBViewHolder(viewBinding)

    class AreasViewHolder(viewBinding: ViewBinding) : DataVBViewHolder(viewBinding)

}