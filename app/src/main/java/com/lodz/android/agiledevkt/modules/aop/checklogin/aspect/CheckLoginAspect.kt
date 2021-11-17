package com.lodz.android.agiledevkt.modules.aop.checklogin.aspect

import android.content.Context
import com.lodz.android.agiledevkt.modules.aop.checklogin.AopLoginActivity
import com.lodz.android.agiledevkt.modules.aop.checklogin.LoginHelper
import com.lodz.android.corekt.anko.toastShort
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

/**
 * 登录代理切面
 * @author zhouL
 * @date 2021/11/11
 */
@Aspect
class CheckLoginAspect {

    /** 切点，【* *(..)】可以处理CheckLogin这个类所有的方法 */
    @Pointcut("execution(@com.lodz.android.agiledevkt.modules.aop.checklogin.aspect.CheckLogin  * *(..))")
    fun executionCheckLogin(){}

    /**
     *  Before	前置通知, 在目标执行之前执行通知
     *   After	后置通知, 目标执行后执行通知
     *   Around	环绕通知, 在目标执行中执行通知, 控制目标执行时机
     *   AfterReturning	后置返回通知, 目标返回时执行通知
     *   AfterThrowing	异常通知, 目标抛出异常时执行通知
     */
    @Around("executionCheckLogin()")
    fun checkLogin(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as? MethodSignature
        val checkLogin = signature?.method?.getAnnotation(CheckLogin::class.java) ?: return joinPoint.proceed()
        val context = joinPoint.`this` as? Context ?: return joinPoint.proceed()
        if (LoginHelper.isUserLogin()) {
            context.toastShort("已登录")
            return joinPoint.proceed()
        }
        context.toastShort("未登录，请先登录")
        return AopLoginActivity.start(context)
    }
}