package com.lodz.android.agiledevkt.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.corekt.anko.getColorCompat
import java.util.*

/**
 * 主页的列表
 * Created by zhouL on 2018/6/28.
 */
class MainAdapter(context: Context) : BaseRecyclerViewAdapter<String>(context) {

    private val COLORS = arrayOf(R.color.color_00a0e9, R.color.color_ea8380, R.color.color_ea413c, R.color.color_303f9f,
            R.color.color_ff4081, R.color.color_d28928, R.color.color_464646)

    private val EMOJI_UNICODE = arrayOf(0x1F601, 0x1F602, 0x1F603, 0x1F604, 0x1F605, 0x1F606, 0x1F609, 0x1F60A,
            0x1F60B, 0x1F60C, 0x1F60D, 0x1F60E, 0x1F60F, 0x1F612, 0x1F613, 0x1F614, 0x1F616, 0x1F618, 0x1F61A,
            0x1F61C, 0x1F61D, 0x1F61E, 0x1F620, 0x1F621, 0x1F622, 0x1F623, 0x1F624, 0x1F625, 0x1F628, 0x1F629,
            0x1F62A, 0x1F62B, 0x1F62D, 0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F634, 0x1F635, 0x1F637, 0x1F638,
            0x1F639, 0x1F63A, 0x1F63B, 0x1F63C, 0x1F63D, 0x1F63E, 0x1F63F)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder(getLayoutView(parent, R.layout.item_main))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val name: String? = getItem(position)
        if (name == null) {
            return
        }
        showItem(holder as MainViewHolder, name)
    }

    private fun showItem(holder: MainViewHolder, name: String) {
        val random = Random()
        holder.nameTv.setTextColor(context.getColorCompat(COLORS[(random.nextInt(100) + 1) % COLORS.size]))
        val str = String(Character.toChars(EMOJI_UNICODE[(random.nextInt(100) + 1) % EMOJI_UNICODE.size])) + "   " + name
        holder.nameTv.text = str
    }

    class MainViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        /** 名称 */
        @BindView(R.id.name)
        lateinit var nameTv: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}