package com.lodz.android.pandora.utils.viewbinding

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * 控件绑定
 * Created by zhouL on 2018/9/27.
 */

/** 在Activity中绑定layout */
fun <VB : ViewBinding> Activity.bindingLayout(inflate: (LayoutInflater) -> VB): Lazy<VB> = lazy {
    return@lazy inflate(layoutInflater)
}

/** 在Dialog中绑定layout */
fun <VB : ViewBinding> Dialog.bindingLayout(inflate: (LayoutInflater) -> VB): Lazy<VB> = lazy {
    return@lazy inflate(layoutInflater)
}

/** 通过上下文绑定layout */
fun <VB : ViewBinding> Context.bindingLayout(inflate: (LayoutInflater) -> VB): Lazy<VB> = lazy {
    return@lazy inflate(LayoutInflater.from(this))
}

/** 通过LayoutInflater绑定layout */
fun <VB : ViewBinding> LayoutInflater.bindingLayout(
    inflate: (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> VB,
    parent: ViewGroup,
    attachToRoot: Boolean = false
): Lazy<VB> = lazy {
    return@lazy inflate(this, parent, attachToRoot)
}

/** 通过Context绑定layout */
fun <VB : ViewBinding> Context.bindingLayout(
    inflate: (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> VB,
    parent: ViewGroup,
    attachToRoot: Boolean = false
): Lazy<VB> = lazy {
    return@lazy inflate(LayoutInflater.from(this), parent, attachToRoot)
}