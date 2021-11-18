package com.lodz.android.agiledevkt.modules.aop.fastclick


/**
 * 快速点击限制
 * @author zhouL
 * @date 2021/11/11
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
/** 快速点击限制，间隔时间[duration]，默认1000毫秒 */
annotation class FastClickLimit(val duration: Long = 1000)
