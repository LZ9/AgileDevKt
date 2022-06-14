package com.lodz.android.agiledevkt.modules.api.coroutines

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.apiservice.ApiCoroutinesService
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.request.BaseRequestBean
import com.lodz.android.agiledevkt.utils.api.ApiServiceManager
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper

/**
 * 协程模式的ViewModel
 * @author zhouL
 * @date 2021/12/14
 */
class ApiCoroutinesViewModel :BaseViewModel(){

    var mResponseResult = MutableLiveData<String>()

    fun requestMock(context: Context) {
        CoroutinesWrapper.create(this)
            .request { ApiServiceManager.get().create(ApiCoroutinesService::class.java).login("admin", "1234") }
            .actionPg(context, context.getString(R.string.mvvm_demo_loading), true) {
                onSuccess { mResponseResult.value = it.data.toString()}

                onError { e, isNetwork ->
                    val msg = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
                    mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, msg)
                }
            }
    }

    fun requestPostSpot(context: Context, id: Int) {
        CoroutinesWrapper.create(this)
            .request { ApiModuleSuspend.postSpot(id) }
            .actionPg(context, context.getString(R.string.mvvm_demo_loading), true){
                onSuccess {
                    val data = it.data
                    if (data != null) {
                        mResponseResult.value = "spotName : ${data.name} ; score : ${data.score}"
                    }
                }

                onError { e, isNetwork ->
                    mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
                }
            }
    }

    fun requestGetSpot(context: Context, id: Int) {
        CoroutinesWrapper.create(this)
            .request { ApiModuleSuspend.getSpot(id) }
            .actionPg(context, context.getString(R.string.mvvm_demo_loading), true){
                onSuccess {
                    val data = it.data
                    if (data != null) {
                        mResponseResult.value = "spotName : ${data.name} ; score : ${data.score}"
                    }
                }
                onError { e, isNetwork ->
                    mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
                }
            }
    }

    fun requestCustom(context: Context) {
        val bean = SpotBean()
        bean.name = "植物园"
        CoroutinesWrapper.create(this)
            .request { ApiModuleSuspend.querySpot(BaseRequestBean.createRequestBody(bean)) }
            .actionPg(context, context.getString(R.string.mvvm_demo_loading), true){
                onSuccess {
                    val data = it.data
                    if (data != null) {
                        mResponseResult.value = "spotName : ${data[0].name} ; score : ${data[0].score}"
                    }
                }
                onError { e, isNetwork ->
                    mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
                }
            }
    }

}