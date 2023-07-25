package com.lodz.android.agiledevkt.modules.datastore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityDataStoreBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * DataStore测试类
 * @author zhouL
 * @date 2023/7/25
 */
class DataStoreActivity : BaseActivity() {
    companion object {
        fun start(context: Context){
            val intent = Intent(context, DataStoreActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDataStoreBinding by bindingLayout(ActivityDataStoreBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}