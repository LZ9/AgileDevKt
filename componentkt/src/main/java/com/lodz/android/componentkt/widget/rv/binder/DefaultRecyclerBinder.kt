package com.lodz.android.componentkt.widget.rv.binder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * 默认的RecyclerBinder
 * Created by zhouL on 2018/12/10.
 */
class DefaultRecyclerBinder(context: Context, binderType: Int) : RecyclerBinder<String>(context, binderType) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = DefaultViewHolder(LinearLayout(getContext()))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getCount(): Int = 0

    override fun getData(position: Int): String = ""

    private inner class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}