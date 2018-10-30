package com.lodz.android.componentkt.base.fragment

import com.trello.rxlifecycle3.components.support.RxFragment


/**
 * 懒加载的fragment
 * Created by zhouL on 2018/6/20.
 */
class LazyFragment : RxFragment(), IFragmentBackPressed {
    override fun onPressBack() = false
}