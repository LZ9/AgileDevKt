package com.lodz.android.corekt.threadpool

/**
 * 优先级线程
 * Created by zhouL on 2018/7/6.
 */
class PriorityThread(runnable: Runnable?) : Thread(runnable) {

    /** android下线程优先级 */
    private var mOSPriority: Int = android.os.Process.THREAD_PRIORITY_DEFAULT

    /** 设置android下线程优先级[priority]，-20到19，高优先级到低优先级 */
    internal fun setOSPriority(priority: Int) {
        mOSPriority = priority
    }

    override fun run() {
        android.os.Process.setThreadPriority(mOSPriority)
        super.run()
    }

}