package com.lodz.android.agiledevkt.modules.datastore

import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDataStoreCaseBinding
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * DataStore协程测试类
 * @author zhouL
 * @date 2023/7/25
 */
abstract class DataStoreBaseActivity : BaseActivity() {

    protected val mBinding: ActivityDataStoreCaseBinding by bindingLayout(ActivityDataStoreCaseBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.datastore_coroutines)
    }

    override fun onClickBackBtn() {
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.idCtv.setOnJumpClickListener {
            updateId()
        }

        mBinding.nameCtv.setOnJumpClickListener {
            updateName()
        }

        mBinding.ageCtv.setOnJumpClickListener {
            updateAge()
        }

        mBinding.heightCtv.setOnJumpClickListener {
            updateHeight()
        }

        mBinding.postgraduateCtv.setOnJumpClickListener {
            updatePostgraduate()
        }

        mBinding.salaryCtv.setOnJumpClickListener {
            updateSalary()
        }

        mBinding.hobbyCtv.setOnJumpClickListener {
            updateHobby()
        }

        mBinding.cleanBtn.setOnClickListener {
            cleanData()
        }
    }

    override fun initData() {
        super.initData()
        updateUI()
        showStatusCompleted()
    }

    protected abstract fun updateUI()

    protected abstract fun updateId()

    protected abstract fun updateName()

    protected abstract fun updateAge()

    protected abstract fun updateHeight()

    protected abstract fun updatePostgraduate()

    protected abstract fun updateSalary()

    protected abstract fun updateHobby()

    protected abstract fun cleanData()
}