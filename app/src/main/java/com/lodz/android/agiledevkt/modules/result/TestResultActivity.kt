package com.lodz.android.agiledevkt.modules.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityTestResultBinding
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.vp2.SimpleVp2Adapter

/**
 * 测试Activity
 * @author zhouL
 * @date 2022/4/11
 */
class TestResultActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TestResultActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityTestResultBinding by bindingLayout(ActivityTestResultBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.arc_test_activity_title)
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
         val tabList = arrayListOf("测试1", "测试2")
        list.add(TestResultAFragment.newInstance(tabList[0]))
        list.add(TestResultBFragment.newInstance(tabList[1]))
        mBinding.viewPager.adapter = SimpleVp2Adapter(this, list)
        mBinding.viewPager.offscreenPageLimit = tabList.size
        mBinding.viewPager.setCurrentItem(0, true)
        mBinding.tabLayout.setupViewPager(mBinding.viewPager, tabList)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.resultBtn.setOnClickListener {
            val str = mBinding.inputEdit.text.toString()
            if (str.isEmpty()) {
                toastShort(R.string.arc_input_hint)
                return@setOnClickListener
            }
            val intent = Intent()
            intent.putExtra(Activity.RESULT_OK.toString(), str)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}