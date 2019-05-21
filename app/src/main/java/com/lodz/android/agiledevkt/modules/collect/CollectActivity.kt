package com.lodz.android.agiledevkt.modules.collect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltEditView
import com.lodz.android.pandora.widget.collect.CltTextView
import kotlin.random.Random

/**
 * 采集测试页
 * @author zhouL
 * @date 2019/3/18
 */
class CollectActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
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
    /** 兴趣 */
    private val mHobbyCedit by bindView<CltEditView>(R.id.hobby_cedit)


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
            val list = arrayListOf(getString(R.string.clt_name_zs), getString(R.string.clt_name_ls), getString(R.string.clt_name_ww))
            list.forEachIndexed { index, name ->
                if (mNameCltv.getContentText().equals(name)) {
                    mNameCltv.setContentText(list.get((index + 1) % 3))
                    return@setOnJumpClickListener
                }
            }
        }

        mSexCltv.setOnContentClickListener {
            mSexCltv.setContentText(if (mSexCltv.getContentText().equals("男")) "女" else "男")
        }

        mAgeCltv.setOnContentClickListener {
            mAgeCltv.setContentText((Random.nextInt(50) + 1).toString())
        }

        mHobbyCedit.setOnInputTextLimit { s, start, before, count, max ->
            toastShort("只能输入${max}字")
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}