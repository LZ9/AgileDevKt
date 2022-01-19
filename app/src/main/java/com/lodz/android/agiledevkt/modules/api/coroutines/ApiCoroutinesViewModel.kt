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
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIOPg

/**
 * 协程模式的ViewModel
 * @author zhouL
 * @date 2021/12/14
 */
class ApiCoroutinesViewModel :BaseViewModel(){


    var mResponseResult = MutableLiveData<String>()

    fun requestMock(context: Context) {
        runOnSuspendIOPg(
            context,
            context.getString(R.string.mvvm_demo_loading),
            cancelable = true,
            request = {
                ApiServiceManager.get().create(ApiCoroutinesService::class.java).login("admin", "1234")
            },
            actionIO = {
                mResponseResult.value = it.data ?: ""
            }, error = { e, isNetwork ->
                val msg = RxUtils.getExceptionTips(e, isNetwork, "加载失败")
                mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
            })
    }

    fun requestPostSpot(context: Context, id: Int) {
        runOnSuspendIOPg(
            context,
            context.getString(R.string.mvvm_demo_loading),
            cancelable = true,
            request = {
                ApiModuleSuspend.postSpot(id)
            },
            actionIO = {
                val data = it.data
                if (data != null){
                    mResponseResult.value = "spotName : ${data.name} ; score : ${data.score}"
                }
            }, error = { e, isNetwork ->
                mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
            })
    }

    fun requestGetSpot(context: Context, id: Int) {
        runOnSuspendIOPg(
            context,
            context.getString(R.string.mvvm_demo_loading),
            cancelable = true,
            request = {
                ApiModuleSuspend.getSpot(id)
            },
            actionIO = {
                val data = it.data
                if (data != null){
                    mResponseResult.value = "spotName : ${data.name} ; score : ${data.score}"
                }
            }, error = { e, isNetwork ->
                mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
            })
    }

    fun requestCustom(context: Context) {
        val bean = SpotBean()
        bean.name = "植物园"
        runOnSuspendIOPg(
            context,
            context.getString(R.string.mvvm_demo_loading),
            cancelable = true,
            request = {
                ApiModuleSuspend.querySpot(BaseRequestBean.createRequestBody(bean))
            },
            actionIO = {
                val data = it.data
                if (data != null){
                    mResponseResult.value = "spotName : ${data[0].name} ; score : ${data[0].score}"
                }
            }, error = { e, isNetwork ->
                mResponseResult.value = RxUtils.getExceptionTips(e, isNetwork, context.getString(R.string.api_fail))
            })
    }

}