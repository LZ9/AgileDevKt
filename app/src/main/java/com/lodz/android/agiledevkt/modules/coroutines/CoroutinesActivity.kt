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
import kotlinx.coroutines.flow.*
import kotlin.random.Random
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
    /** 基础flow */
    private val TYPE_SIMPLE_FLOW = 8
    /** flow的launchIn */
    private val TYPE_FLOW_LAUNCH_IN = 9
    /** flow的操作符 */
    private val TYPE_FLOW_ACTION = 10
    /** flow的buffer */
    private val TYPE_FLOW_BUFFER= 11
    /** flow聚合操作 */
    private val TYPE_FLOW_ZIP= 12
    /** flow的异常 */
    private val TYPE_FLOW_THROWABLE= 13


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
                R.id.simple_flow_rbtn -> TYPE_SIMPLE_FLOW
                R.id.flow_launchin_rbtn -> TYPE_FLOW_LAUNCH_IN
                R.id.flow_action_rbtn -> TYPE_FLOW_ACTION
                R.id.flow_buffer_rbtn -> TYPE_FLOW_BUFFER
                R.id.flow_zip_rbtn -> TYPE_FLOW_ZIP
                R.id.flow_throwable_rbtn -> TYPE_FLOW_THROWABLE
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
            if (mType == TYPE_JOIN) {
                runOnSuspendIO {
                    mJob = join()
                }
                return@setOnClickListener
            }
            mJob = when (mType) {
                TYPE_MAIN -> main()
                TYPE_IO -> io()
                TYPE_REPEAT -> repeat()
                TYPE_TIMEOUT -> timeout()
                TYPE_CHANNEL -> channel()
                TYPE_ASYNC -> async()
                TYPE_SIMPLE_FLOW -> simpleFlow()
                TYPE_FLOW_LAUNCH_IN -> flowLaunchIn()
                TYPE_FLOW_ACTION -> flowAction()
                TYPE_FLOW_BUFFER -> flowBuffer()
                TYPE_FLOW_ZIP -> flowZip()
                TYPE_FLOW_THROWABLE -> flowThrowable()
                else -> null
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
        logResult(Thread.currentThread().name, "开始")
        val job = MainScope().launch(Dispatchers.IO) {
            logResult(Thread.currentThread().name, "开始执行协程")
            delay(1000L)
            logResult(Thread.currentThread().name, "完成")
            logResult(Thread.currentThread().name, "结束协程")
        }

        logResult(Thread.currentThread().name, "结束")
        return job
    }

    /** 等待协程执行，join阻塞主线程 */
    private suspend fun join(): Job {
        val job = MainScope().launch(Dispatchers.IO) {
            delay(1000L)
            logResult(Thread.currentThread().name, "开始子协程")
            delay(3000L)
            logResult(Thread.currentThread().name, "结束子协程")
            delay(1000L)
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
    private fun timeout(): Job = runOnSuspendIOCatch({
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
    private fun async(): Job = runOnIO {
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

    /** 基础的流方法 */
    private fun simpleFlow(): Job = MainScope().launch {
        simple(5, 500)
            .map {
                val reslut = it + 1
                logResult(Thread.currentThread().name,"+ map $reslut")
                reslut
            }
            .flowOn(Dispatchers.IO)//上面的流在IO线程执行
            .collect {
                // 订阅在main还是io取决于最外面是什么线程
                logResult(Thread.currentThread().name,"collect $it")
            }

        // flow会在flowOn指定的线程中执行
        flow { emit(getRandomNum()) }
            .flowOn(Dispatchers.IO)
            .collect { logResult(Thread.currentThread().name, "flow $it") }

        // flowOf会在当前线程执行，不受flowOn的限制
        flowOf(getRandomNum())
            .flowOn(Dispatchers.IO)
            .collect { logResult(Thread.currentThread().name, "flowOf $it") }
    }

    private fun getRandomNum(): Int {
        val i = Random.nextInt(10)
        logResult(Thread.currentThread().name, "i $i")
        return i
    }

    /** 简单的流，从1开始遍历数字[num]，延时[delay]毫秒 */
    private suspend fun simple(num: Int, delay: Long): Flow<Int> = flow {
        for (i in 1..num) {
            delay(delay)
            logResult(Thread.currentThread().name,"simple emit $i , delay : $delay")
            emit(i)
        }
    }

    /** 在同一个线程并发连个流操作 */
    private fun flowLaunchIn(): Job = MainScope().launch {
        simple(4, 400)
            .flowOn(Dispatchers.IO)
            .onEach { logResult(Thread.currentThread().name, "collect a $it") }
            .launchIn(this)

        simple(6, 1000)
            .flowOn(Dispatchers.IO)
            .onEach { logResult(Thread.currentThread().name, "collect b $it") }
            .launchIn(this)
    }

    /** 流的操作符 */
    private fun flowAction(): Job = MainScope().launch {
        val list = (1..5).asFlow()
            .map { it * 2 }
            .toList()
        logResult(Thread.currentThread().name, "flowToList : $list")
        val sum = list.asFlow()
            .take(list.size - 1)
            .filter { it > 5 }
            .reduce { accumulator, value -> accumulator + value }
        logResult(Thread.currentThread().name, "sum : $sum")
    }

    /** Buffer操作符 */
    private fun flowBuffer(): Job = IoScope().launch {

        val time1 = measureTimeMillis {
            simple(3, 100)
                .collect {
                    delay(300)
                    logResult(Thread.currentThread().name, "collect : $it")
                }
        }
        logResult(Thread.currentThread().name, "time : $time1")

        logResult(Thread.currentThread().name, "-- 通过buffer将数据收集后再一起订阅 --")

        val time2 = measureTimeMillis {
            simple(3, 100)
                .buffer()
                .collect {
                    delay(300)
                    logResult(Thread.currentThread().name, "collect buffer : $it")
                }
        }
        logResult(Thread.currentThread().name, "time buffer : $time2")
    }

    /** flow聚合操作 */
    private fun flowZip(): Job = MainScope().launch {
        val nums = (1..3).asFlow().onEach { delay(300) } //每300毫秒发射一条
        val strs = flowOf("one", "two", "three").onEach { delay(400) } //每400毫秒发射一条

        val time1 = measureTimeMillis {
            nums.zip(strs) { a, b ->
                "$a -> $b"
            }.collect {
                logResult(Thread.currentThread().name, "collect : $it")
            }
        }
        logResult(Thread.currentThread().name, "time zip : $time1")

        val time2 = measureTimeMillis {
            nums.combine(strs) { a, b ->
                "$a -> $b"
            }.collect {
                logResult(Thread.currentThread().name, "collect : $it")
            }
        }
        logResult(Thread.currentThread().name, "time combine : $time2")
    }

    /** flow的异常 */
    private fun flowThrowable(): Job = MainScope().launch {
        simple(5, 200)
            .map {
                check(it <= 4){"$it is > 4"}
                it
            }
            .catch { e ->
                logResult(Thread.currentThread().name, "Exception : $e")
            }
            .collect {
                logResult(Thread.currentThread().name, "collect : $it")
            }
    }

    private fun logResult(threadName: String, log: String) {
        synchronized(this) {
            runOnMain {
                val enter = if (mBinding.resultTv.text.isEmpty()) "" else "\n"
                mBinding.resultTv.text = StringBuilder(mBinding.resultTv.text).append(enter)
                    .append(threadName)
                    .append(" ---> ")
                    .append(log)
                    .toString()
            }
        }
    }

    private fun cleanResult() {
        mBinding.resultTv.text = ""
    }
}

