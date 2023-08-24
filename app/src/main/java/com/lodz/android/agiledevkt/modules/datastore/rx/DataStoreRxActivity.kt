package com.lodz.android.agiledevkt.modules.datastore.rx

import android.content.Context
import android.content.Intent
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.modules.datastore.DataStoreBaseActivity
import com.lodz.android.agiledevkt.utils.ds.DsManager
import com.lodz.android.corekt.anko.AnkoNumFormat
import com.lodz.android.corekt.anko.format
import com.lodz.android.corekt.utils.AppUtils
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.subscriber.BaseSubscriber
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.rx.utils.cleanRx
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
        Flowable.just(Random.nextDouble(10.0) + 1)
            .flatMap { DsManager.setHeightRx((180 - it).format(AnkoNumFormat.TYPE_ONE_DECIMAL).toFloat()).rx() }
            .flatMap { DsManager.getHeightRx().rx() }
            .compose(RxUtils.ioToMainFlowable())
            .subscribe(BaseSubscriber.action(next = {
                mBinding.heightCtv.setContentText(it.toString())
            }))
    }

    override fun updatePostgraduate() {
        Flowable.just("")
            .flatMap { DsManager.setPostgraduateRx(!DsManager.getPostgraduateRx().sync()).rx() }
            .flatMap { DsManager.getPostgraduateRx().rx() }
            .compose(RxUtils.ioToMainFlowable())
            .subscribe(BaseSubscriber.action(next = {
                mBinding.postgraduateCtv.setContentText(it.toString())
            }))
    }

    override fun updateSalary() {
        Flowable.just(Random.nextDouble(3000.0) + 1)
            .flatMap { DsManager.setSalaryRx((10000 - it).format().toDouble()).rx() }
            .flatMap { DsManager.getSalaryRx().rx() }
            .subscribe(BaseSubscriber.action(next = {
                mBinding.salaryCtv.setContentText(it.toString())
            }))
    }

    override fun updateHobby() {
        Flowable.just(setOf("看书", "写字", "画画", "游泳", "足球", "篮球", "乒乓球", "台球", "棒球", "田径"))
            .map {
                val length = Random.nextInt(it.size) + 1
                val hobbySet = hashSetOf<String>()
                for (i in 0 until length) {
                    hobbySet.add(it.elementAt(i))
                }
                return@map hobbySet
            }
            .flatMap { DsManager.setHobbyRx(it).rx() }
            .flatMap { DsManager.getHobbyRx().rx() }
            .subscribe(BaseSubscriber.action(next = {
                mBinding.hobbyCtv.setContentText(it.toString())
            }))
    }

    override fun cleanData() {
        App.get().dataStore.cleanRx().rx()
            .compose(RxUtils.ioToMainObservable())
            .subscribe(BaseObserver.action(next = {
                updateUI()
            }))
    }
}