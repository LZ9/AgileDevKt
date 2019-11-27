package com.lodz.android.agiledevkt.modules.coroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnIO
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
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


    /** 单选组 */
    private val mRadioGroup by bindView<RadioGroup>(R.id.radio_group)

    /** 启动按钮 */
    private val mStartBtn by bindView<MaterialButton>(R.id.start_btn)
    /** 取消按钮 */
    private val mCancelBtn by bindView<MaterialButton>(R.id.cancel_btn)
    /** 清空按钮 */
    private val mCleanBtn by bindView<MaterialButton>(R.id.clean_btn)

    /** 日志 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    /** 类型 */
    private var mType = TYPE_NONE
    /** 协程工作 */
    private var mJob: Job? = null

    override fun getLayoutId(): Int = R.layout.activity_coroutines

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

        mRadioGroup.setOnCheckedChangeListener { group, checkedId ->
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

        mStartBtn.setOnClickListener {
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

        mCancelBtn.setOnClickListener {
            mJob?.cancel()
        }

        mCleanBtn.setOnClickListener {
            cleanResult()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 主线程执行协程 */
    private fun main(): Job {
        return GlobalScope.launch(Dispatchers.Main) {
            logResult("${Thread.currentThread().name} ---> 开始执行协程")
            for (i in 10 downTo 1) {
                logResult(i.toString())
                delay(500)
            }
            logResult("完成")
            logResult("${Thread.currentThread().name} ---> 结束协程")
        }
    }

    /** 异步线程执行协程 */
    private fun io(): Job {
        val job = GlobalScope.launch {
            logResult("${Thread.currentThread().name} ---> 开始执行协程")
            delay(1000L)
            logResult("完成")
            logResult("${Thread.currentThread().name} ---> 结束协程")
        }
        logResult("开始")
        logResult("${Thread.currentThread().name} ---> 结束")
        return job
    }

    /** 等待协程执行，join阻塞主线程 */
    private suspend fun join(): Job {
        val job = GlobalScope.launch {
            logResult("${Thread.currentThread().name} ---> 开始执行协程")
            delay(5000L)
            logResult("完成")
            logResult("${Thread.currentThread().name} ---> 结束协程")
        }
        logResult("开始")
        job.join() // 等待直到子协程执行结束
        logResult("${Thread.currentThread().name} ---> 结束")
        return job
    }

    /** 协程执行重复数据 */
    private fun repeat(): Job {
        return GlobalScope.launch {
            logResult("${Thread.currentThread().name} ---> 开始执行协程")
            repeat(12) { i ->
                logResult("${Thread.currentThread().name} ---> $i")
                delay(500)
            }
            logResult("${Thread.currentThread().name} ---> 结束协程")
        }
    }

    /** 超时限制 */
    private fun timeout(): Job {
        return GlobalScope.launch {
            withTimeout(1500) {
                logResult("${Thread.currentThread().name} ---> 开始执行协程")
                repeat(12) { i ->
                    logResult("${Thread.currentThread().name} ---> $i")
                    delay(400)
                }
                logResult("${Thread.currentThread().name} ---> 结束协程")
            }
        }
    }

    /** 协程通道 */
    private fun channel(): Job {
        return GlobalScope.launch {
            logResult("${Thread.currentThread().name} ---> 开始执行协程")
            val channel = Channel<Int>()
            launch {
                for (i in 1..12) {
                    if (i % 2 == 0) {
                        channel.send(i)
                    }
                    delay(400)
                }
                channel.close()
            }
            for (c in channel) {// 遍历通道内的元素，直到通道被关闭
                logResult("${Thread.currentThread().name} ---> $c")
            }
            logResult("${Thread.currentThread().name} ---> 结束协程")
        }
    }

    /** 协程异步 */
    private fun async(): Job? {
        GlobalScope.runOnIO {
            logResult("顺序执行开始")
            val time1 = measureTimeMillis {
                runBlocking {
                    val one = getNum(12, 1200)
                    val two = getNum(32, 400)
                    logResult("one + two = ${one + two}")
                }
            }
            logResult("顺序执行耗时：$time1")
            logResult("顺序执行结束")

            logResult("-----------")

            logResult("异步执行开始")
            val time2 = measureTimeMillis {
                runBlocking {
                    val one: Deferred<Int> = async { getNum(12, 1200) }
                    val two: Deferred<Int> = async { getNum(32, 400) }
                    logResult("one + two = ${one.await() + two.await()}")
                }
            }
            logResult("异步执行耗时：$time2")
            logResult("异步执行结束")

            logResult("-----------")

            logResult("懒加载异步执行开始")
            val time3 = measureTimeMillis {
                runBlocking {
                    val one: Deferred<Int> = async(start = CoroutineStart.LAZY) { getNum(12, 1200) }
                    val two: Deferred<Int> = async(start = CoroutineStart.LAZY) { getNum(32, 400) }
                    one.start()
                    delay(1300)
                    two.start()
                    logResult("one + two = ${one.await() + two.await()}")
                }
            }
            logResult("懒加载异步执行耗时：$time3")
            logResult("懒加载异步执行结束")
        }
        return null
    }

    /** 获取数字[num]，延时[delay]毫秒 */
    private suspend fun getNum(num: Int, delay: Long): Int {
        delay(delay)
        return num
    }

    private fun logResult(log: String) {
        GlobalScope.runOnMain {
            mResultTv.text = StringBuilder(mResultTv.text).append("\n").append(log).toString()
        }
    }

    private fun cleanResult() {
        mResultTv.text = ""
    }

}

