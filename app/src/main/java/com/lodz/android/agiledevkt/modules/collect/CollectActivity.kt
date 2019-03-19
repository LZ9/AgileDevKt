package com.lodz.android.agiledevkt.modules.collect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltTextView

/**
 * 采集测试页
 * @author zhouL
 * @date 2019/3/18
 */
class CollectActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, CollectActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 姓名 */
    private val mNameCltv by bindView<CltTextView>(R.id.name_cltv)
    /** 性别 */
    private val mSexCltv by bindView<CltTextView>(R.id.sex_cltv)
    /** 年龄 */
    private val mAgeCltv by bindView<CltTextView>(R.id.age_cltv)

    override fun getLayoutId(): Int = R.layout.activity_collect

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.clt_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mNameCltv.setOnContentClickListener {
            toastShort("click NameCltv content")
        }

        mNameCltv.setOnJumpClickListener {
            toastShort("click NameCltv jump")
        }

        mSexCltv.setOnContentClickListener {
            toastShort("click SexCltv content")
        }

        mAgeCltv.setOnContentClickListener {
            toastShort("click AgeCltv content")
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}