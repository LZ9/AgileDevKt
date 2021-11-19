package com.lodz.android.agiledevkt.modules.aop.fastclick

import android.view.View
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

    companion object{
        /** 默认点击间隔时间 */
        const val DEFAULT_DURATION = 1000L
    }

    /** 上一次的记录时间 */
    private var mLastTime: Long = 0

    /** 注解的切点表达式 */
    @Pointcut("execution(@com.lodz.android.agiledevkt.modules.aop.fastclick.FastClickLimit  * *(..))")
    fun dealViewClickWithAnnotation(){}


    @Around("dealViewClickWithAnnotation()")
    fun fastClickLimit(joinPoint: ProceedingJoinPoint) {
        val signature = joinPoint.signature as? MethodSignature
        val fastClickLimit = signature?.method?.getAnnotation(FastClickLimit::class.java)
        if (fastClickLimit == null) {
            joinPoint.proceed()
            return
        }
        val time = System.currentTimeMillis()
        if (time - mLastTime > fastClickLimit.duration){
            mLastTime = time
            joinPoint.proceed()
        }
    }

    /** 匿名内部类的切点表达式 */
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    fun dealViewClickWithObject() {}

    /** Lambda的切点表达式 */
    @Pointcut("execution(* *..*lambda*(.., android.view.View))")
    fun dealViewClickWithLambda() {}

    @Around("dealViewClickWithObject()||dealViewClickWithLambda()")
    fun clickListenerLimit(joinPoint: ProceedingJoinPoint) {
        val signature = joinPoint.signature as? MethodSignature
        val fastClickLimit = signature?.method?.getAnnotation(FastClickLimit::class.java)
        if (fastClickLimit != null) {//如果已经注解了FastClickLimit就跳过不处理
            joinPoint.proceed()
            return
        }
        val view = getClickView(joinPoint.args)
        if (view == null) {
            joinPoint.proceed()
            return
        }
        val key = view.id
        val currentTime: Long = view.getTag(key) as? Long ?: 0
        val time = System.currentTimeMillis()
        if (time - currentTime > DEFAULT_DURATION){
            view.setTag(key, time)
            joinPoint.proceed()
        }
    }

    /** 获取参数[args]中的View对象 */
    private fun getClickView(args: Array<Any>?): View? {
        if (args.isNullOrEmpty()) {
            return null
        }
        args.forEach {
            if (it is View) {
                return it
            }
        }
        return null
    }

}