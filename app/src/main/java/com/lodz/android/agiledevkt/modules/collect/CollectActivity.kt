package com.lodz.android.agiledevkt.modules.collect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
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
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mNameCltv.setOnContentClickListener {
            toastShort(mNameCltv.getContentText())
        }

        mNameCltv.setOnJumpClickListener {
            toastShort(mNameCltv.getJumpBtnText())
        }

        mSexCltv.setOnContentClickListener {
            toastShort(mSexCltv.getContentHint())
        }

        mAgeCltv.setOnContentClickListener {
            toastShort(mAgeCltv.getContentHint())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}