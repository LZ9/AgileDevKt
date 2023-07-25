package com.lodz.android.agiledevkt.modules.datastore.coroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDataStoreCoroutinesBinding
import com.lodz.android.agiledevkt.modules.datastore.dataStore
import com.lodz.android.corekt.anko.IoScope
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * DataStore协程测试类
 * @author zhouL
 * @date 2023/7/25
 */
class DataStoreCoroutinesActivity : BaseActivity() {
    companion object {

        private val KEY_NAME = stringPreferencesKey("key_name")
        private val NAME_LIST = arrayListOf("张三","李四","王五","赵六","孙七")

        private val KEY_AGE = intPreferencesKey("key_age")

        fun start(context: Context){
            val intent = Intent(context, DataStoreCoroutinesActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDataStoreCoroutinesBinding by bindingLayout(ActivityDataStoreCoroutinesBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.datastore_coroutines)
    }

    override fun onClickBackBtn() {
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.updateNameBtn.setOnClickListener {
            updateName()
        }

        mBinding.updateAgeBtn.setOnClickListener {
            updateAge()
        }
    }

    override fun initData() {
        super.initData()
        getName()
        getAge()
        showStatusCompleted()
    }

    private fun updateName(){

        IoScope().launch {
            dataStore.edit {
                var name = createName()
                val oldName = it[KEY_NAME] ?: ""
                while (name == oldName) {
                    name = createName()
                }
                it[KEY_NAME] = name
            }
        }
    }

    private fun createName() = NAME_LIST[Random.nextInt(NAME_LIST.size - 1)]

    private fun getName() {
        IoScope().launch {
//            mBinding.nameCtv.setContentText(dataStore.data.map { it[KEY_NAME] ?: "无" }.first())
            dataStore.data.map { it[KEY_NAME] ?: "无" }
                .collect {
                    mBinding.nameCtv.setContentText(it)
                }
        }
    }

    private fun updateAge() {
        IoScope().launch {
            dataStore.edit {
                var age = createAge()
                val oldAge = it[KEY_AGE] ?: 0
                while (age == oldAge) {
                    age = createAge()
                }
                it[KEY_AGE] = age
            }
        }
    }

    private fun createAge() = Random.nextInt(99) + 1

    private fun getAge() {
        IoScope().launch {
            dataStore.data.map { it[KEY_AGE] ?: 0 }
                .collect {
                    mBinding.ageCtv.setContentText(it.toString())
                }
        }
    }
}