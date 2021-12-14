package com.lodz.android.agiledevkt.modules.api.rx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.apiservice.ApiService
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.request.BaseRequestBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.databinding.ActivityApiRxTestBinding
import com.lodz.android.agiledevkt.modules.api.ApiServiceImpl
import com.lodz.android.agiledevkt.utils.api.ApiServiceManager
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * RX模式的接口请求
 * @author zhouL
 * @date 2021/12/14
 */
class ApiRxTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApiRxTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityApiRxTestBinding by bindingLayout(ActivityApiRxTestBinding::inflate)

    /** 请求类型 */
    private var mRequestType = ApiServiceImpl.API_SUCCESS

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.api_rx)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 网络异常
        mBinding.netSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestType = when {
                isChecked -> ApiServiceImpl.NETWORK_FAIL
                mBinding.dataSwitch.isChecked -> ApiServiceImpl.API_FAIL
                else -> ApiServiceImpl.API_SUCCESS
            }
        }

        // 接口失败
        mBinding.dataSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestType = when {
                isChecked -> ApiServiceImpl.API_FAIL
                mBinding.netSwitch.isChecked -> ApiServiceImpl.NETWORK_FAIL
                else -> ApiServiceImpl.API_SUCCESS
            }
        }

        mBinding.mockBtn.setOnClickListener {
            ApiServiceManager.get().create(ApiService::class.java)
                .login("admin", "1234")
                .compose(bindDestroyEvent())
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : ProgressObserver<ResponseBean<String>>(){
                    override fun onPgNext(any: ResponseBean<String>) {
                        val data = any.data
                        if (!data.isNullOrEmpty()){
                            mBinding.resultTv.text = data
                        }
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        mBinding.resultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                    }
                }.create(getContext(), R.string.api_loading, false))
        }

        mBinding.postBtn.setOnClickListener {
            ApiServiceImpl.postSpot(mRequestType)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(object : ProgressObserver<ResponseBean<SpotBean>>(){
                    override fun onPgNext(any: ResponseBean<SpotBean>) {
                        val data = any.data
                        if (data != null){
                            mBinding.resultTv.text = java.lang.StringBuilder("spotName : ${data.spotName} ; score : ${data.score}")
                        }
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        mBinding.resultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                    }
                }.create(getContext(), R.string.api_loading, false))
        }

        mBinding.getBtn.setOnClickListener {
            ApiServiceImpl.getSpot(mRequestType)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(object : ProgressObserver<ResponseBean<SpotBean>>(){
                    override fun onPgNext(any: ResponseBean<SpotBean>) {
                        val data = any.data
                        if (data != null){
                            mBinding.resultTv.text = java.lang.StringBuilder("spotName : ${data.spotName} ; score : ${data.score}")
                        }
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        mBinding.resultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                    }
                }.create(getContext(), R.string.api_loading, false))
        }

        mBinding.customBtn.setOnClickListener {
            val bean = SpotBean()
            bean.spotName = "植物园"
            ApiServiceImpl.querySpot(BaseRequestBean.createRequestBody(bean))
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(object : ProgressObserver<ResponseBean<List<SpotBean>>>(){
                    override fun onPgNext(any: ResponseBean<List<SpotBean>>) {
                        val data = any.data
                        if (data != null){
                            mBinding.resultTv.text = StringBuilder("spotName : ${data[0].spotName} ; score : ${data[0].score}")
                        }
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        mBinding.resultTv.text = RxUtils.getExceptionTips(e, isNetwork, getString(R.string.api_fail))
                    }
                }.create(getContext(), R.string.api_loading, false))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}