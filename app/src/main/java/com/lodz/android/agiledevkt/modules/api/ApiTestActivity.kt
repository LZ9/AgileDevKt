package com.lodz.android.agiledevkt.modules.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.apiservice.ApiService
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.request.BaseRequestBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.api.ApiServiceManager
import com.lodz.android.corekt.anko.bindView
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

    /** 网络异常 */
    private val mNetSwitch by bindView<Switch>(R.id.net_switch)
    /** 接口失败 */
    private val mDataSwitch by bindView<Switch>(R.id.data_switch)
    /** mock请求 */
    private val mMockBtn by bindView<MaterialButton>(R.id.mock_btn)
    /** Post按钮 */
    private val mPostBtn by bindView<MaterialButton>(R.id.post_btn)
    /** Get按钮 */
    private val mGetBtn by bindView<MaterialButton>(R.id.get_btn)
    /** 自定义按钮 */
    private val mCustomBtn by bindView<MaterialButton>(R.id.custom_btn)
    /** 接口内容 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    /** 请求类型 */
    private var mRequestType = ApiServiceImpl.API_SUCCESS

    override fun getLayoutId(): Int = R.layout.activity_api_test

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

        // 网络异常
        mNetSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestType = when {
                isChecked -> ApiServiceImpl.NETWORK_FAIL
                mDataSwitch.isChecked -> ApiServiceImpl.API_FAIL
                else -> ApiServiceImpl.API_SUCCESS
            }
        }

        // 接口失败
        mDataSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestType = when {
                isChecked -> ApiServiceImpl.API_FAIL
                mNetSwitch.isChecked -> ApiServiceImpl.NETWORK_FAIL
                else -> ApiServiceImpl.API_SUCCESS
            }
        }

        mMockBtn.setOnClickListener {
            ApiServiceManager.get().create(ApiService::class.java)
                    .login("admin", "1234")
                    .compose(bindDestroyEvent())
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(object :ProgressObserver<ResponseBean<String>>(){
                        override fun onPgNext(any: ResponseBean<String>) {
                            val data = any.data
                            if (!data.isNullOrEmpty()){
                                mResultTv.text = data
                            }
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            mResultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                        }
                    }.create(getContext(), R.string.api_loading, false))
        }

        mPostBtn.setOnClickListener {
            ApiServiceImpl.postSpot(mRequestType)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object :ProgressObserver<ResponseBean<SpotBean>>(){
                        override fun onPgNext(any: ResponseBean<SpotBean>) {
                            val data = any.data
                            if (data != null){
                                mResultTv.text = java.lang.StringBuilder("spotName : ${data.spotName} ; score : ${data.score}")
                            }
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            mResultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                        }
                    }.create(getContext(), R.string.api_loading, false))
        }

        mGetBtn.setOnClickListener {
            ApiServiceImpl.getSpot(mRequestType)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object :ProgressObserver<ResponseBean<SpotBean>>(){
                        override fun onPgNext(any: ResponseBean<SpotBean>) {
                            val data = any.data
                            if (data != null){
                                mResultTv.text = java.lang.StringBuilder("spotName : ${data.spotName} ; score : ${data.score}")
                            }
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            mResultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                        }
                    }.create(getContext(), R.string.api_loading, false))
        }

        mCustomBtn.setOnClickListener {
            val bean = SpotBean()
            bean.spotName = "植物园"
            ApiServiceImpl.querySpot(BaseRequestBean.createRequestBody(bean))
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object :ProgressObserver<ResponseBean<List<SpotBean>>>(){
                        override fun onPgNext(any: ResponseBean<List<SpotBean>>) {
                            val data = any.data
                            if (data != null){
                                mResultTv.text = StringBuilder("spotName : ${data[0].spotName} ; score : ${data[0].score}")
                            }
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            mResultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                        }
                    }.create(getContext(), R.string.api_loading, false))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}