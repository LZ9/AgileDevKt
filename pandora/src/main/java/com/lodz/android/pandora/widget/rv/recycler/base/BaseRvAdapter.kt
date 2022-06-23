package com.lodz.android.pandora.widget.rv.recycler.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView基类适配器
 * Created by zhouL on 2018/6/28.
 */
abstract class BaseRvAdapter<T>(context: Context) : AbsRvAdapter<T, RecyclerView.ViewHolder>(context)