package com.lodz.android.pandora.widget.search

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

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
            // 内容布局
            setItemViewHeight(holder.withView<ViewGroup>(R.id.content_layout), bean.getItemHigthPx())
        }

        // 图标
        configIconImg(holder.withView(R.id.icon_img), bean)
        // 标题文字
        configTitle(holder.withView(R.id.title_tv), bean)
        // 标题标签
        configTitleTag(holder.withView(R.id.title_tag_tv), bean)
        // 描述文字
        configDesc(holder.withView(R.id.desc_tv), bean)
        // 文字一标签
        configFirstTag(holder.withView(R.id.first_tag_tv), bean)
        // 文字二标签
        configSecondTag(holder.withView(R.id.second_tag_tv), bean)
        // 提示文字
        configTips(holder.withView(R.id.tips_tv), bean)
        // 分割线
        configDivideLine(holder.withView(R.id.divide_line_view), bean)
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
}