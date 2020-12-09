package com.lodz.android.agiledevkt.modules.koin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import kotlin.random.Random

/**
 * Koin注入测试类
 * @author zhouL
 * @date 2020/12/4
 */
class KoinTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, KoinTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mSingleTv by bindView<TextView>(R.id.single_tv)
    private val mSingleChangeTv by bindView<TextView>(R.id.single_change_tv)
    private val mFactoryTv by bindView<TextView>(R.id.factory_tv)

    private val mSingleBlz by inject<SpotBean>(named("blz"))
    private val mFactoryHdl by inject<SpotBean>(named("hdl")){ parametersOf("4.5")}

    override fun getLayoutId(): Int = R.layout.activity_koin_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        mSingleTv.text = "Single原始对象：${mSingleBlz.spotName}    ${mSingleBlz.score}"
        val num = Random.nextInt(5) + 6
        mSingleBlz.score = num.toString()
        mSingleChangeTv.text = "Single修改 $num 对象：${mSingleBlz.spotName}    ${mSingleBlz.score}"

        mFactoryTv.text = "Factory对象：${mFactoryHdl.spotName}    ${mFactoryHdl.score}"
        showStatusCompleted()
    }

}