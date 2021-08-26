package com.lodz.android.agiledevkt.modules.threadpool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.lodz.android.agiledevkt.databinding.ActivityThreadPoolBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.threadpool.ThreadPoolManager
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlinx.coroutines.MainScope
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 线程池测试类
 * Created by zhouL on 2018/7/17.
 */
class ThreadPoolActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ThreadPoolActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityThreadPoolBinding by bindingLayout(ActivityThreadPoolBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 开始按钮
        mBinding.startBtn.setOnClickListener {
            startLongThread()
            for (i in 0 until 30) {
                startShortThread(i)
            }
        }

        // 关闭按钮
        mBinding.closeBtn.setOnClickListener {
            ThreadPoolManager.get().releaseNormalExecutor()
        }

        // 清空按钮
        mBinding.clearBtn.setOnClickListener {
            mBinding.resultTv.text = ""
        }
    }

    /** 启动长时间线程 */
    private fun startLongThread() {
        ThreadPoolManager.get().executeNormal(Runnable {
            try {
                printResult("long start")
                Thread.sleep(12000)
                printResult("long end")
            } catch (e: Exception) {
                e.printStackTrace()
                printResult("long InterruptedException")
            }
        })
    }

    /** 启动短时间线程，线程标记[index] */
    private fun startShortThread(index: Int) {
        ThreadPoolManager.get().executeNormal(IndexRunnable(index))
    }

    /** 带标记[index]的Runnable内部类 */
    inner class IndexRunnable(private val index: Int) : Runnable {
        override fun run() {
            try {
                printResult("short $index start")
                Thread.sleep(2000)
                printResult("short $index end")
            } catch (e: Exception) {
                e.printStackTrace()
                printResult("short $index InterruptedException")
            }

        }
    }

    override fun initData() {
        super.initData()
        ThreadPoolManager.get().newBuilder()
            .setAwaitTime(50)// 设置线程结束等待时间
            .setAwaitTimeUnit(TimeUnit.MILLISECONDS)// 设置线程结束等待时间单位
            .setKeepAliveTime(1)// 设置线程数空闲时间
            .setKeepAliveTimeUnit(TimeUnit.SECONDS)// 设置线程数空闲时间单位
            .setCorePoolSize(4)// 设置线程数
            .setMaximumPoolSize(8)// 设置最大线程数
            .setRejectedExecutionHandler(ThreadPoolExecutor.DiscardPolicy())// 设置拒绝策略
            .build()
        showStatusCompleted()
    }

    /** 打印信息[result] */
    private fun printResult(result: String) {
        MainScope().runOnMain {
            mBinding.resultTv.text = (mBinding.resultTv.text.toString() + "\n" + result)
        }
        MainScope().runOnMainDelay(100) {
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

}