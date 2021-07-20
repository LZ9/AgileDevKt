package com.lodz.android.agiledevkt.modules.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.databinding.RvItemViewBindingBinding

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/19
 */
class ViewBindingAdapter(private val list: List<String>) : RecyclerView.Adapter<ViewBindingAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder =
        TestViewHolder(RvItemViewBindingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.binding.nameTv.text = list[position]
    }

    override fun getItemCount(): Int = list.size

    inner class TestViewHolder(val binding: RvItemViewBindingBinding) : RecyclerView.ViewHolder(binding.root)

}