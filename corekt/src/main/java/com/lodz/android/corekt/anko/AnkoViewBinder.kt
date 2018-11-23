package com.lodz.android.corekt.anko

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * 控件绑定
 * Created by zhouL on 2018/9/27.
 */

/** 在Activity中绑定view */
fun <T : View> Activity.bindView(id: Int): Lazy<T> = lazy {
    val view = findViewById<T>(id)
    if (view == null) {
        throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
    }
    return@lazy view
}

/** 在Fragment中绑定view */
fun <T : View> Fragment.bindView(id: Int): Lazy<T> = lazy {
    val fragmentView = view
    if (fragmentView == null) {
        throw IllegalStateException("'${javaClass.name}' fragment view is null.")
    }
    val view = fragmentView.findViewById<T>(id)
    if (view == null) {
        throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
    }
    return@lazy view
}

/** 在RecyclerView.ViewHolder中绑定view */
fun <T : View> RecyclerView.ViewHolder.bindView(id: Int): Lazy<T> = lazy {
    val view = itemView.findViewById<T>(id)
    if (view == null) {
        throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
    }
    return@lazy view
}

/** 在Dialog中绑定view */
fun <T : View> Dialog.bindView(id: Int): Lazy<T> = lazy {
    val view = findViewById<T>(id)
    if (view == null) {
        throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
    }
    return@lazy view
}

/** 在ViewGroup中绑定view */
fun <T : View> ViewGroup.bindView(id: Int): Lazy<T> = lazy {
    val view = findViewById<T>(id)
    if (view == null) {
        throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
    }
    return@lazy view
}

/** 在PopupWindow中绑定view */
fun <T : View> PopupWindow.bindView(id: Int): Lazy<T> = lazy {
    val view = contentView.findViewById<T>(id)
    if (view == null) {
        throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
    }
    return@lazy view
}
