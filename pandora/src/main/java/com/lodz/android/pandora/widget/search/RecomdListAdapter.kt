package com.lodz.android.pandora.widget.search

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 搜索联想列表适配器
 * @author zhouL
 * @date 2019/10/25
 */
class RecomdListAdapter(context: Context) : BaseRecyclerViewAdapter<RecomdData>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataViewHolder(getLayoutView(parent, R.layout.pandora_item_search_recomd))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data == null || holder !is DataViewHolder) {
            return
        }
        showItem(holder, data)
    }

    private fun showItem(holder: DataViewHolder, bean: RecomdData) {
        if (bean.getItemBackgroundDrawableRes() != 0){
            holder.itemView.setBackgroundResource(bean.getItemBackgroundDrawableRes())
        }
        if (bean.getItemBackgroundColor() != 0){
            holder.itemView.setBackgroundColor(bean.getItemBackgroundColor())
        }
        if (bean.getItemHigthPx() != 0){
            setItemViewHeight(holder.contentLayout, bean.getItemHigthPx())
        }

        configIconImg(holder.iconImg, bean)
        configTitle(holder.titleTv, bean)
        configTitleTag(holder.titleTagTv, bean)
        configDesc(holder.descTv, bean)
        configFirstTag(holder.firstTagTv, bean)
        configSecondTag(holder.secondTagTv, bean)
        configTips(holder.tipsTv, bean)
        configDivideLine(holder.divideLineView, bean)
    }

    /** 配置图标 */
    private fun configIconImg(iconImg: ImageView, bean: RecomdData) {
        if (bean.getIconDrawableRes() != 0) {
            iconImg.setImageResource(bean.getIconDrawableRes())
        }
        iconImg.visibility = bean.getIconVisibility()
    }

    /** 配置标题文字 */
    private fun configTitle(titleTv: TextView, bean: RecomdData) {
        titleTv.visibility = if (bean.getTitleText().isEmpty()) View.GONE else View.VISIBLE
        titleTv.text = bean.getTitleText()
        if (bean.getTitleTextColor() != 0) {
            titleTv.setTextColor(bean.getTitleTextColor())
        }
        if (bean.getTitleTextSize() != 0f) {
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.getTitleTextSize())
        }
    }

    /** 配置标题标签 */
    private fun configTitleTag(titleTagTv: TextView, bean: RecomdData) {
        titleTagTv.visibility = if (bean.getTitleTagText().isEmpty()) View.GONE else View.VISIBLE
        titleTagTv.text = bean.getTitleTagText()
        if (bean.getTitleTagTextColor() != 0) {
            titleTagTv.setTextColor(bean.getTitleTagTextColor())
        }
        if (bean.getTitleTagTextSize() != 0f) {
            titleTagTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.getTitleTagTextSize())
        }
        if (bean.getTitleTagTextBackground() != 0) {
            titleTagTv.setBackgroundResource(bean.getTitleTagTextBackground())
        }
    }

    /** 配置描述文字 */
    private fun configDesc(descTv: TextView, bean: RecomdData) {
        descTv.visibility = if (bean.getDescText().isEmpty()) View.GONE else View.VISIBLE
        descTv.text = bean.getDescText()
        if (bean.getDescTextColor() != 0) {
            descTv.setTextColor(bean.getDescTextColor())
        }
        if (bean.getDescTextSize() != 0f) {
            descTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.getDescTextSize())
        }
    }

    /** 配置文字一标签 */
    private fun configFirstTag(firstTagTv: TextView, bean: RecomdData) {
        firstTagTv.visibility = if (bean.getFirstTagText().isEmpty()) View.GONE else View.VISIBLE
        firstTagTv.text = bean.getFirstTagText()
        if (bean.geFirstTagTextColor() != 0) {
            firstTagTv.setTextColor(bean.geFirstTagTextColor())
        }
        if (bean.getFirstTagTextSize() != 0f) {
            firstTagTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.getFirstTagTextSize())
        }
        if (bean.getFirstTagTextBackground() != 0) {
            firstTagTv.setBackgroundResource(bean.getFirstTagTextBackground())
        }
    }

    /** 配置文字二标签 */
    private fun configSecondTag(secondTagTv: TextView, bean: RecomdData) {
        secondTagTv.visibility = if (bean.getSecondTagText().isEmpty()) View.GONE else View.VISIBLE
        secondTagTv.text = bean.getSecondTagText()
        if (bean.getSecondTagTextColor() != 0) {
            secondTagTv.setTextColor(bean.getSecondTagTextColor())
        }
        if (bean.getSecondTagTextSize() != 0f) {
            secondTagTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.getSecondTagTextSize())
        }
        if (bean.getSecondTagTextBackground() != 0) {
            secondTagTv.setBackgroundResource(bean.getSecondTagTextBackground())
        }
    }

    /** 配置标题标签 */
    private fun configTips(tipsTv: TextView, bean: RecomdData) {
        tipsTv.visibility = if (bean.getTipsText().isEmpty()) View.GONE else View.VISIBLE
        tipsTv.text = bean.getTipsText()
        if (bean.getTipsTextColor() != 0) {
            tipsTv.setTextColor(bean.getTipsTextColor())
        }
        if (bean.getTipsTextSize() != 0f) {
            tipsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.getTipsTextSize())
        }
    }

    /** 配置分割线 */
    private fun configDivideLine(divideLineView: View, bean: RecomdData) {
        divideLineView.visibility = bean.getDivideLineVisibility()
        if (bean.getDivideLineColor() != 0) {
            divideLineView.setBackgroundColor(bean.getDivideLineColor())
        }
    }

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /** 内容布局 */
        val contentLayout by bindView<ViewGroup>(R.id.content_layout)
        /** 图标 */
        val iconImg by bindView<ImageView>(R.id.icon_img)
        /** 标题文字 */
        val titleTv by bindView<TextView>(R.id.title_tv)
        /** 标题标签 */
        val titleTagTv by bindView<TextView>(R.id.title_tag_tv)
        /** 描述文字 */
        val descTv by bindView<TextView>(R.id.desc_tv)
        /** 文字一标签 */
        val firstTagTv by bindView<TextView>(R.id.first_tag_tv)
        /** 文字二标签 */
        val secondTagTv by bindView<TextView>(R.id.second_tag_tv)
        /** 提示文字 */
        val tipsTv by bindView<TextView>(R.id.tips_tv)
        /** 分割线 */
        val divideLineView by bindView<View>(R.id.divide_line_view)
    }
}