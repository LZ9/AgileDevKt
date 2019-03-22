package com.lodz.android.agiledevkt.modules.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.apiservice.ApiService
import com.lodz.android.agiledevkt.bean.base.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.api.ApiServiceManager
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils

/**
 * 接口测试类
 * @author zhouL
 * @date 2019/3/22
 */
class ApiTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApiTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** Post按钮 */
    private val mPostBtn by bindView<MaterialButton>(R.id.post_btn)

    override fun getLayoutId(): Int = R.layout.activity_api_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mPostBtn.setOnClickListener {
            ApiServiceManager.get().create(ApiService::class.java)
                    .login("admin", "1234")
                    .compose(bindDestroyEvent())
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(object :ProgressObserver<ResponseBean<String>>(){
                        override fun onPgNext(any: ResponseBean<String>) {
                            val data = any.data
                            if (!data.isNullOrEmpty()){
                                toastShort(data)
                            }
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            toastShort(RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail)))
                        }
                    }.create(getContext(), R.string.api_loading, false))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}