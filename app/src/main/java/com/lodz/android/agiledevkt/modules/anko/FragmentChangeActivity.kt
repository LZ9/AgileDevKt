package com.lodz.android.agiledevkt.modules.anko

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.anko.fragment.AnkoAccountFragment
import com.lodz.android.agiledevkt.modules.anko.fragment.AnkoDetailFragment
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * Fragment变更类
 * Created by zhouL on 2018/11/20.
 */
class FragmentChangeActivity : BaseActivity() {

    companion object {
        private const val EXTRA_ACCOUNT = "extra_account"
        private const val EXTRA_PASSWORD = "extra_password"

        fun start(context: Context, account: String, pswd: String) {
            val intent = Intent(context, FragmentChangeActivity::class.java)
            intent.putExtra(EXTRA_ACCOUNT, account)
            intent.putExtra(EXTRA_PASSWORD, pswd)
            context.startActivity(intent)
        }
    }

    /** 账号页 */
    private lateinit var mAnkoAccountFragment : AnkoAccountFragment
    /** 详情页 */
    private lateinit var mAnkoDetailFragment : AnkoDetailFragment

    private var mAccount = ""

    override fun startCreate() {
        super.startCreate()
        mAccount = intent.getStringExtra(EXTRA_ACCOUNT)
    }

    override fun getLayoutId(): Int = R.layout.activity_fragment_change

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.al_fg_change_title)
        initFragment()
    }

    private fun initFragment() {
        mAnkoAccountFragment = AnkoAccountFragment.newInstance(mAccount)
        mAnkoDetailFragment = AnkoDetailFragment.newInstance()

        addFragment(R.id.root_up_layout, mAnkoAccountFragment, "AnkoAccountFragment")
        addFragment(R.id.root_down_layout, mAnkoDetailFragment, "AnkoDetailFragment")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mAnkoAccountFragment.setOnChangeListener {
            showDetail()
        }

        mAnkoDetailFragment.setOnChangeListener {
            showAccount()
        }
    }

    override fun initData() {
        super.initData()
        showAccount()
        showStatusCompleted()
    }

    /** 显示账号 */
    private fun showAccount(){
        showFragment(mAnkoAccountFragment)
        hideFragment(mAnkoDetailFragment)
    }

    /** 显示详情 */
    private fun showDetail(){
        showFragment(mAnkoDetailFragment)
        hideFragment(mAnkoAccountFragment)
    }
}