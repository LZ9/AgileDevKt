package com.lodz.android.agiledevkt.modules.rv.tree

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.city.AreasBean
import com.lodz.android.agiledevkt.bean.city.CityBean
import com.lodz.android.agiledevkt.bean.city.ProvinceBean
import com.lodz.android.agiledevkt.databinding.ActivityRvTreeBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import com.lodz.android.pandora.utils.jackson.parseJsonObject
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setup

/**
 * Rv树结构测试类
 * @author zhouL
 * @date 2022/7/15
 */
class TreeRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, TreeRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mAdapter: TreeRvAdapter

    private val mBinding: ActivityRvTreeBinding by bindingLayout(ActivityRvTreeBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    /** 标题 */
    private val mTitle by intentExtrasNoNull(MainActivity.EXTRA_TITLE_NAME, "")
    /** 数据列表 */
    private var mList: ArrayList<ProvinceBean> = arrayListOf()

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(mTitle)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.treeRv
            .linear()
            .setup(TreeRvAdapter(getContext()))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            if (item is ProvinceBean){
                toastShort(item.provinceName)
            }
            if (item is CityBean){
                toastShort(item.cityName)
            }
            if (item is AreasBean){
                toastShort(item.areaName)
            }
        }

        mAdapter.setOnItemLongClickListener { viewHolder, item, position ->
            if (item is ProvinceBean){
                showSnackbar(item.provinceName)
            }
            if (item is CityBean){
                showSnackbar(item.cityName)
            }
            if (item is AreasBean){
                showSnackbar(item.areaName)
            }
        }

        mAdapter.setOnTreeChangedListener {
            toastShort("size : ${it.size}")
        }

        mBinding.collapsedAllBtn.setOnClickListener {
            toastShort("size : ${mList.size}")
        }

        mBinding.expandAllBtn.setOnClickListener {
            toastShort("size : ${mList.size}")
        }
    }

    private fun showSnackbar(msg: String) {
        SnackbarUtils.createShort(mBinding.root, msg)
            .setBackgroundColor(getColorCompat(R.color.color_a0191919)).show()
    }

    override fun initData() {
        super.initData()
        getTestData()
    }

    private fun getTestData() {
        CoroutinesWrapper.create(this)
            .request { getAssetsFileContent("city.json") }
            .action {
                onSuccess {
                    mList = it.parseJsonObject()
                    mAdapter.setTreeData(mList)
                    showStatusCompleted()
                }
            }
    }

}