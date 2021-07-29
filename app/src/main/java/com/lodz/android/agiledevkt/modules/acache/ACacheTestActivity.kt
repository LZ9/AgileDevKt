package com.lodz.android.agiledevkt.modules.acache

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityAcacheTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.cache.ACache
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.acache.ACacheUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * ACache缓存测试类
 * Created by zhouL on 2018/11/8.
 */
class ACacheTestActivity : BaseActivity() {

    companion object {
        /** 缓存键 */
        private const val CACHE_KEY = "cache_key"

        fun start(context: Context) {
            val intent = Intent(context, ACacheTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityAcacheTestBinding by bindingLayout(ActivityAcacheTestBinding::inflate)

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

        // 获取缓存
        mBinding.getBtn.setOnClickListener {
            val cache = ACacheUtils.get().create().getAsString(CACHE_KEY)
            mBinding.resultTv.text = StringBuilder("结果： $cache")
        }

        // 缓存无时间限制
        mBinding.foreverBtn.setOnClickListener {
            if (!isCanSave()) {
                toastShort("请输入缓存内容")
                return@setOnClickListener
            }
            ACacheUtils.get().create().put(CACHE_KEY, mBinding.contentEdit.text.toString())
        }

        // 缓存5秒
        mBinding.secondBtn.setOnClickListener {
            if (!isCanSave()) {
                toastShort("请输入缓存内容")
                return@setOnClickListener
            }
            ACacheUtils.get().create().put(CACHE_KEY, mBinding.contentEdit.text.toString(), 5)
        }

        // 缓存1天
        mBinding.dayBtn.setOnClickListener {
            if (!isCanSave()) {
                toastShort("请输入缓存内容")
                return@setOnClickListener
            }
            ACacheUtils.get().create().put(CACHE_KEY, mBinding.contentEdit.text.toString(), 1 * ACache.TIME_DAY)
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun isCanSave(): Boolean = mBinding.contentEdit.text.isNotEmpty()
}