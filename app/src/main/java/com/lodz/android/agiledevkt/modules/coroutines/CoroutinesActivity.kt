package com.lodz.android.agiledevkt.modules.coroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCoroutinesBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.system.measureTimeMillis

/**
 * 协程测试类
 * @author zhouL
 * @date 2019/5/14
 */
class CoroutinesActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CoroutinesActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 未选择类型 */
    private val TYPE_NONE = 0
    /** 主线程执行 */
    private val TYPE_MAIN = 1
    /** 异步线程执行 */
    private val TYPE_IO = 2
    /** 阻塞主线程等待协程执行 */
    private val TYPE_JOIN = 3
    /** 协程执行重复数据 */
    private val TYPE_REPEAT = 4
    /** 超时限制 */
    private val TYPE_TIMEOUT = 5
    /** 协程通道 */
    private val TYPE_CHANNEL = 6
    /** 协程异步 */
    private val TYPE_ASYNC = 7

    private val mBinding: ActivityCoroutinesBinding by bindingLayout(ActivityCoroutinesBinding::inflate)

    /** 类型 */
    private var mType = TYPE_NONE
    /** 协程工作 */
    private var mJob: Job? = null

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 单选组
        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            mType = when (checkedId) {
                R.id.main_rbtn -> TYPE_MAIN
                R.id.io_rbtn -> TYPE_IO
                R.id.join_rbtn -> TYPE_JOIN
                R.id.repeat_rbtn -> TYPE_REPEAT
                R.id.timeout_rbtn -> TYPE_TIMEOUT
                R.id.channel_rbtn -> TYPE_CHANNEL
                R.id.async_rbtn -> TYPE_ASYNC
                else -> TYPE_NONE
            }
        }

        // 启动按钮
        mBinding.startBtn.setOnClickListener {
            if (mType == TYPE_NONE) {
                toastShort(R.string.coroutines_type_none_tips)
                return@setOnClickListener
            }
            cleanResult()
            if (mType == TYPE_MAIN) {
                mJob = main()
                return@setOnClickListener
            }
            if (mType == TYPE_IO) {
                mJob = io()
                return@setOnClickListener
            }
            if (mType == TYPE_JOIN) {
                runBlocking {
                    mJob = join()
                }
                return@setOnClickListener
            }
            if (mType == TYPE_REPEAT) {
                mJob = repeat()
                return@setOnClickListener
            }
            if (mType == TYPE_TIMEOUT) {
                mJob = timeout()
                return@setOnClickListener
            }
            if (mType == TYPE_CHANNEL) {
                mJob = channel()
                return@setOnClickListener
            }
            if (mType == TYPE_ASYNC) {
                mJob = async()
                return@setOnClickListener
            }

        }

        // 取消按钮
        mBinding.cancelBtn.setOnClickListener {
            mJob?.cancel()
        }

        // 清空按钮
        mBinding.cleanBtn.setOnClickListener {
            cleanResult()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 主线程执行协程 */
    private fun main(): Job {
        return MainScope().launch(Dispatchers.Main) {
            logResult(Thread.currentThread().name, "开始执行协程")
            for (i in 10 downTo 1) {
                logResult(Thread.currentThread().name, i.toString())
                delay(500)
            }
            logResult(Thread.currentThread().name, "完成")
            logResult(Thread.currentThread().name, "结束协程")
        }
    }

    /** 异步线程执行协程 */
    private fun io(): Job {
        val job = MainScope().launch(Dispatchers.IO) {
            logResult(Thread.currentThread().name, "开始执行协程")
            delay(1000L)
            logResult(Thread.currentThread().name, "完成")
            logResult(Thread.currentThread().name, "结束协程")
        }
        logResult(Thread.currentThread().name, "开始")
        logResult(Thread.currentThread().name, "结束")
        return job
    }

    /** 等待协程执行，join阻塞主线程 */
    private suspend fun join(): Job {
        val job = MainScope().launch(Dispatchers.IO) {
            logResult(Thread.currentThread().name, "开始执行协程")
            delay(3000L)
            logResult(Thread.currentThread().name, "完成")
            logResult(Thread.currentThread().name, "结束协程")
        }
        logResult(Thread.currentThread().name, "开始")
        job.join() // 等待直到子协程执行结束
        logResult(Thread.currentThread().name, "结束")
        return job
    }

    /** 协程执行重复数据 */
    private fun repeat(): Job = MainScope().launch(Dispatchers.IO) {
        logResult(Thread.currentThread().name, "开始执行协程")
        repeat(12) { i ->
            logResult(Thread.currentThread().name, "$i")
            delay(500)
        }
        logResult(Thread.currentThread().name, "结束协程")
    }

    /** 超时限制 */
    private fun timeout(): Job = MainScope().runOnSuspendIOCatch({
        withTimeout(1500) {
            logResult(Thread.currentThread().name, "开始执行协程")
            repeat(12) { i ->
                logResult(Thread.currentThread().name, "$i")
                delay(400)
            }
            logResult(Thread.currentThread().name, "结束协程")
        }
    }, {
        logResult(Thread.currentThread().name, "已超时,${it.message}")
    })

    /** 协程通道 */
    private fun channel(): Job = MainScope().launch(Dispatchers.IO) {
        logResult(Thread.currentThread().name, "开始执行协程")
        val channel = Channel<Int>()
        launch {
            logResult(Thread.currentThread().name, "设置通道数据")
            for (i in 1..12) {
                if (i % 2 == 0) {
                    channel.send(i)
                }
                delay(400)
            }
            logResult(Thread.currentThread().name, "设置通道数据完成")
            channel.close()//通道只有关闭才会终止遍历
        }
        logResult(Thread.currentThread().name, "开始遍历")
        for (c in channel) {// 遍历通道内的元素，直到通道被关闭
            logResult(Thread.currentThread().name, "$c")
        }
        logResult(Thread.currentThread().name, "结束协程")
    }

    /** 协程异步 */
    private fun async(): Job = MainScope().runOnIO {
        logResult(Thread.currentThread().name,"顺序执行开始")
        val time1 = measureTimeMillis {
            runBlocking {
                val one = getNum(12, 1200)
                val two = getNum(32, 400)
                // 顺序执行one 和 two ，延时为：1200 + 400
                logResult(Thread.currentThread().name,"one + two = ${one + two}")
            }
        }
        logResult(Thread.currentThread().name,"顺序执行耗时：$time1")
        logResult(Thread.currentThread().name,"顺序执行结束")

        logResult(Thread.currentThread().name,"-----------")

        logResult(Thread.currentThread().name,"异步执行开始")
        val time2 = measureTimeMillis {
            runBlocking {
                val one: Deferred<Int> = async { getNum(12, 1200) }
                val two: Deferred<Int> = async { getNum(32, 400) }
                logResult(Thread.currentThread().name, "延迟1300秒开始")
                delay(1300)
                logResult(Thread.currentThread().name, "延迟1300秒结束")
                // 异步执行one 和 two （创建后就立即执行），延时为：1300
                logResult(Thread.currentThread().name,"one + two = ${one.await() + two.await()}")
            }
        }
        logResult(Thread.currentThread().name,"异步执行耗时：$time2")
        logResult(Thread.currentThread().name,"异步执行结束")

        logResult(Thread.currentThread().name,"-----------")

        logResult(Thread.currentThread().name, "懒加载异步执行开始")
        val time3 = measureTimeMillis {
            runBlocking {
                val one: Deferred<Int> = async(start = CoroutineStart.LAZY) { getNum(12, 1200) }
                val two: Deferred<Int> = async(start = CoroutineStart.LAZY) { getNum(32, 400) }
                one.start()
                logResult(Thread.currentThread().name, "延迟1300秒开始")
                delay(1300)
                logResult(Thread.currentThread().name, "延迟1300秒结束")
                two.start()
                // 异步执行one 和 two （创建后不会立即执行，只有调用start方法才会执行），延时为：1300 + 400
                logResult(Thread.currentThread().name,"one + two = ${one.await() + two.await()}")
            }
        }
        logResult(Thread.currentThread().name,"懒加载异步执行耗时：$time3")
        logResult(Thread.currentThread().name,"懒加载异步执行结束")
    }

    /** 获取数字[num]，延时[delay]毫秒 */
    private suspend fun getNum(num: Int, delay: Long): Int {
        logResult(Thread.currentThread().name,"$num 延迟 $delay 开始")
        delay(delay)
        logResult(Thread.currentThread().name,"$num 延迟 $delay 结束")
        return num
    }

    private fun logResult(threadName: String, log: String) {
        MainScope().runOnMain {
            mBinding.resultTv.text = StringBuilder(mBinding.resultTv.text).append("\n")
                .append(threadName)
                .append(" ---> ")
                .append(log)
                .toString()
        }
    }

    private fun cleanResult() {
        mBinding.resultTv.text = ""
    }
}

