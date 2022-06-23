package com.lodz.android.agiledevkt.modules.rv.anim

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder
import kotlin.random.Random

/**
 * RV动画测试适配器
 * Created by zhouL on 2018/11/23.
 */
class AnimRvAdapter(context: Context) : BaseRvAdapter<String>(context) {

    private val EMOJI_UNICODE = arrayOf(0x1F601, 0x1F602, 0x1F603, 0x1F604, 0x1F605, 0x1F606, 0x1F609, 0x1F60A,
            0x1F60B, 0x1F60C, 0x1F60D, 0x1F60E, 0x1F60F, 0x1F612, 0x1F613, 0x1F614, 0x1F616, 0x1F618, 0x1F61A,
            0x1F61C, 0x1F61D, 0x1F61E, 0x1F620, 0x1F621, 0x1F622, 0x1F623, 0x1F624, 0x1F625, 0x1F628, 0x1F629,
            0x1F62A, 0x1F62B, 0x1F62D, 0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F634, 0x1F635, 0x1F637, 0x1F638,
            0x1F639, 0x1F63A, 0x1F63B, 0x1F63C, 0x1F63D, 0x1F63E, 0x1F63F)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_anim))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val name = getItem(position)
        if (name.isNullOrEmpty() || holder !is DataViewHolder) {
            return
        }
        showItem(holder, name, position)
    }

    private fun showItem(holder: DataViewHolder, time: String, position: Int) {
        holder.withView<TextView>(R.id.emoji_tv).text = String(Character.toChars(EMOJI_UNICODE[(Random.nextInt(100) + 1) % EMOJI_UNICODE.size]))
        holder.withView<TextView>(R.id.position_tv).text = (position + 1).toString()
        holder.withView<TextView>(R.id.time_tv).text = time
    }
}