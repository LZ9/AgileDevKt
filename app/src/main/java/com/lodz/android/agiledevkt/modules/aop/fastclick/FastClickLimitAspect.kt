package com.lodz.android.agiledevkt.modules.aop.fastclick

import com.lodz.android.corekt.log.PrintLog
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature

/**
 * 快速点击限制切面
 * @author zhouL
 * @date 2021/11/11
 */
@Aspect
class FastClickLimitAspect {

    private var mCurrentTime: Long = 0

    /**
     *  Before	前置通知, 在目标执行之前执行通知
     *   After	后置通知, 目标执行后执行通知
     *   Around	环绕通知, 在目标执行中执行通知, 控制目标执行时机
     *   AfterReturning	后置返回通知, 目标返回时执行通知
     *   AfterThrowing	异常通知, 目标抛出异常时执行通知
     */
    @Around("execution(@com.lodz.android.agiledevkt.modules.aop.fastclick.FastClickLimit  * *(..))")
    fun fastClickLimit(joinPoint: ProceedingJoinPoint) {
        PrintLog.d("testtag", "fastClickLimit")
        val signature = joinPoint.signature as? MethodSignature
        val fastClickLimit = signature?.method?.getAnnotation(FastClickLimit::class.java)
        if (fastClickLimit == null) {
            joinPoint.proceed()
            return
        }
        val time = System.currentTimeMillis()
        if (time - mCurrentTime > fastClickLimit.duration){
            mCurrentTime = time
            joinPoint.proceed()
        }
        PrintLog.i("testtag", "time : $time")
    }



//    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
//    fun limitOnClickListener(joinPoint: ProceedingJoinPoint): Any? {
//        PrintLog.i("testtag", "OnClickListener")
//        return joinPoint.proceed()
//    }


}