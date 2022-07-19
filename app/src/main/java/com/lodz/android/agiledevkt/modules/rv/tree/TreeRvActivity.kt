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
import com.lodz.android.pandora.widget.rv.tree.RvTreeItem

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
    /** 厦门城市数据 */
    private var mXmCityBean: CityBean? = null
    /** 福建省数据 */
    private var mFjProvinceBean: ProvinceBean? = null
    /** 数据列表 */
    private var mList: ArrayList<RvTreeItem> = arrayListOf()

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

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        initData()
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
            mList.clear()
            mList.addAll(it)
        }

        mBinding.collapsedAllBtn.setOnClickListener {
            mAdapter.collapsedAll()
        }

        mBinding.expandAllBtn.setOnClickListener {
            mAdapter.expandAll()
        }

        mBinding.collapsedXmBtn.setOnClickListener {
            val fjBean = mFjProvinceBean ?: return@setOnClickListener
            val xmBean = mXmCityBean ?: return@setOnClickListener
            for (item in mList) {
                if (item is ProvinceBean && item.provinceId == fjBean.provinceId) {
                    for (cityBean in item.citys) {
                        if (cityBean.cityId == xmBean.cityId) {
                            item.isExpand = false
                            cityBean.isExpand = false
                            mAdapter.setTreeDataObj(mList)
                            return@setOnClickListener
                        }
                    }
                }
            }
        }

        mBinding.expandXmBtn.setOnClickListener {
            val fjBean = mFjProvinceBean ?: return@setOnClickListener
            val xmBean = mXmCityBean ?: return@setOnClickListener
            for (item in mList) {
                if (item is ProvinceBean && item.provinceId == fjBean.provinceId) {
                    for (cityBean in item.citys) {
                        if (cityBean.cityId == xmBean.cityId) {
                            item.isExpand = true
                            cityBean.isExpand = true
                            mAdapter.setTreeDataObj(mList)
                            return@setOnClickListener
                        }
                    }
                }
            }
        }

        mBinding.expandEvenBtn.setOnClickListener {
            mAdapter.collapsedAll()
            for (item in mList) {
                if (item is ProvinceBean) {
                    item.isExpand = item.provinceId.toInt() % 2 == 1
                }
            }
            mAdapter.setTreeDataObj(mList)
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
            .request {
                val json = getAssetsFileContent("city.json")
                val list = json.parseJsonObject<ArrayList<ProvinceBean>>()
                for (provinceBean in list) {
                    for (cityBean in provinceBean.citys) {
                        if (cityBean.cityName == "厦门市") {
                            mXmCityBean = cityBean
                            mFjProvinceBean = provinceBean
                        }
                    }
                }
                list
            }
            .action {
                onSuccess {
                    mList.clear()
                    mList.addAll(it)
                    mAdapter.setTreeData(it)
                    showStatusCompleted()
                }
                onError { e, isNetwork ->
                    showStatusError(e)
                    toastShort(e.toString())
                }
            }
    }

}