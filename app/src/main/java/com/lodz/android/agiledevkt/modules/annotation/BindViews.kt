package com.lodz.android.agiledevkt.modules.annotation

import androidx.annotation.IdRes

/**
 * 绑定控件注解
 * @author zhouL
 * @date 2019/5/29
 */

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class BindViews(@IdRes val id: Int)