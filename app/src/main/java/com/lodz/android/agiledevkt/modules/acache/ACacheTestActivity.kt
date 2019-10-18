package com.lodz.android.agiledevkt.modules.acache

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.cache.ACache
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.acache.ACacheUtils

/**
 * ACache缓存测试类
 * Created by zhouL on 2018/11/8.
 */
class ACacheTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ACacheTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 缓存键 */
    private val CACHE_KEY = "cache_key"

    /** 结果显示 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 缓存输入框 */
    private val mContentEdit by bindView<EditText>(R.id.content_edit)
    /** 获取缓存 */
    private val mGetBtn by bindView<Button>(R.id.get_btn)
    /** 缓存无时间限制 */
    private val mForeverBtn by bindView<Button>(R.id.forever_btn)
    /** 缓存5秒 */
    private val mSecondBtn by bindView<Button>(R.id.second_btn)
    /** 缓存1天 */
    private val mDayBtn by bindView<Button>(R.id.day_btn)

    override fun getLayoutId(): Int = R.layout.activity_acache_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mGetBtn.setOnClickListener {
            val cache = ACacheUtils.get().create().getAsString(CACHE_KEY)
            mResultTv.text = StringBuilder("结果： $cache")
        }

        mForeverBtn.setOnClickListener {
            if (!isCanSave()) {
                toastShort("请输入缓存内容")
                return@setOnClickListener
            }
            ACacheUtils.get().create().put(CACHE_KEY, mContentEdit.text.toString())
        }

        mSecondBtn.setOnClickListener {
            if (!isCanSave()) {
                toastShort("请输入缓存内容")
                return@setOnClickListener
            }
            ACacheUtils.get().create().put(CACHE_KEY, mContentEdit.text.toString(), 5)
        }

        mDayBtn.setOnClickListener {
            if (!isCanSave()) {
                toastShort("请输入缓存内容")
                return@setOnClickListener
            }
            ACacheUtils.get().create().put(CACHE_KEY, mContentEdit.text.toString(), 1 * ACache.TIME_DAY)
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun isCanSave(): Boolean = mContentEdit.text.isNotEmpty()
}