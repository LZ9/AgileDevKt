package com.lodz.android.agiledevkt.modules.aop.checklogin.aspect


/**
 * 登录校验注解
 * @author zhouL
 * @date 2021/11/11
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckLogin()
