package com.lodz.android.agiledevkt.modules.datastore.rx

import android.content.Context
import android.content.Intent
import com.lodz.android.agiledevkt.modules.datastore.DataStoreBaseActivity
import com.lodz.android.agiledevkt.utils.ds.DsManager
import com.lodz.android.corekt.utils.AppUtils
import com.lodz.android.pandora.rx.subscribe.subscriber.BaseSubscriber
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.rxjava3.core.Flowable
import kotlin.random.Random

/**
 * DataStore Rx测试类
 * @author zhouL
 * @date 2023/7/25
 */
class DataStoreRxActivity : DataStoreBaseActivity() {
    companion object {
        fun start(context: Context){
            val intent = Intent(context, DataStoreRxActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun updateUI() {
        mBinding.apply {
            idCtv.setContentText(DsManager.getIdRx().sync().toString())
            nameCtv.setContentText(DsManager.getNameRx().sync())
            ageCtv.setContentText(DsManager.getAgeRx().sync().toString())
            heightCtv.setContentText(DsManager.getHeightRx().sync().toString())
            postgraduateCtv.setContentText(DsManager.getPostgraduateRx().sync().toString())
            salaryCtv.setContentText(DsManager.getSalaryRx().sync().toString())
            hobbyCtv.setContentText(DsManager.getHobbyRx().sync().toString())
        }
    }

    override fun updateId() {
        var id = AppUtils.getUUID32().hashCode().toLong()
        if (id < 0) {
            id *= -1
        }
        Flowable.just(id)
            .flatMap { DsManager.setIdRx(it).rx() }
            .flatMap { DsManager.getIdRx().rx() }
            .compose(RxUtils.ioToMainFlowable())
            .subscribe(BaseSubscriber.action(next = {
                mBinding.idCtv.setContentText(it.toString())
            }))
    }

    override fun updateName() {
        Flowable.just(arrayListOf("张三", "李四", "王五", "赵六", "孙七", "周八", "吴九", "郑十"))
            .flatMap { DsManager.setNameRx(it[Random.nextInt(it.size - 1)]).rx() }
            .flatMap { DsManager.getNameRx().rx() }
            .compose(RxUtils.ioToMainFlowable())
            .subscribe(BaseSubscriber.action(next = {
                mBinding.nameCtv.setContentText(it)
            }))
    }

    override fun updateAge() {
        Flowable.just(Random.nextInt(99) + 1)
            .flatMap { DsManager.setAgeRx(it).rx() }
            .flatMap { DsManager.getAgeRx().rx() }
            .compose(RxUtils.ioToMainFlowable())
            .subscribe(BaseSubscriber.action(next = {
                mBinding.ageCtv.setContentText(it.toString())
            }))
    }

    override fun updateHeight() {
        TODO("Not yet implemented")
    }

    override fun updatePostgraduate() {
        TODO("Not yet implemented")
    }

    override fun updateSalary() {
        TODO("Not yet implemented")
    }

    override fun updateHobby() {
        TODO("Not yet implemented")
    }

    override fun cleanData() {
        TODO("Not yet implemented")
    }



}