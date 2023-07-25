package com.lodz.android.agiledevkt.modules.datastore.rx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDataStoreRxBinding
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Single

/**
 * DataStore Rx测试类
 * @author zhouL
 * @date 2023/7/25
 */
class DataStoreRxActivity : BaseActivity() {
    companion object {

        private val KEY_AGE = intPreferencesKey("key_age")

        fun start(context: Context){
            val intent = Intent(context, DataStoreRxActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDataStoreRxBinding by bindingLayout(ActivityDataStoreRxBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private val mDataStore: RxDataStore<Preferences> = RxPreferenceDataStoreBuilder(getContext(), "settings").build()

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.datastore_rx)
    }

    override fun onClickBackBtn() {
        finish()
    }

    override fun initData() {
        super.initData()
        mDataStore.data().map { it.get(KEY_AGE) ?: 0 }.subscribe()

        mDataStore.updateDataAsync {
            val mutablePreferences = it.toMutablePreferences()
            val currentInt = it.get(KEY_AGE)
            mutablePreferences.set(KEY_AGE, if (currentInt != null) currentInt + 1 else 1)
            return@updateDataAsync Single.just(mutablePreferences)
        }.subscribe()
        showStatusCompleted()
    }
}