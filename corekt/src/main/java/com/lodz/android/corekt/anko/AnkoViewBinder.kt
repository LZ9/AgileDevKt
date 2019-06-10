package com.lodz.android.corekt.anko

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * 控件绑定
 * Created by zhouL on 2018/9/27.
 */

/** 在Activity中绑定view */
fun <T : View> Activity.bindView(id: Int): Lazy<T> = lazy {
    return@lazy findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
}

/** 在Fragment中绑定view */
fun <T : View> Fragment.bindView(id: Int): Lazy<T> = lazy {
    val fragmentView = view ?: throw IllegalStateException("'${javaClass.name}' fragment view is null.")
    return@lazy fragmentView.findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
}

/** 在RecyclerView.ViewHolder中绑定view */
fun <T : View> RecyclerView.ViewHolder.bindView(id: Int): Lazy<T> = lazy {
    return@lazy itemView.findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
}

/** 在Dialog中绑定view */
fun <T : View> Dialog.bindView(id: Int): Lazy<T> = lazy {
    return@lazy findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
}

/** 在ViewGroup中绑定view */
fun <T : View> View.bindView(id: Int): Lazy<T> = lazy {
    return@lazy findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
}

/** 在PopupWindow中绑定view */
fun <T : View> PopupWindow.bindView(id: Int): Lazy<T> = lazy {
    return@lazy contentView.findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
}
