package com.lodz.android.agiledevkt.modules.download

import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.bean.AppInfoBean
import com.lodz.android.agiledevkt.databinding.ActivityDownloadMarketBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setup
import zlc.season.rxdownload4.manager.TaskManager
import zlc.season.rxdownload4.manager.delete
import zlc.season.rxdownload4.manager.start
import zlc.season.rxdownload4.manager.stop

/**
 * 下载测试
 * @author zhouL
 * @date 2020/6/14
 */
class DownloadMarketActivity :BaseActivity() {

    private lateinit var mAdapter: DownloadMarketAdapter

    private val mBinding: ActivityDownloadMarketBinding by bindingLayout(ActivityDownloadMarketBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.recyclerView
            .linear()
            .setup(DownloadMarketAdapter(getContext()))
            .apply {
                this.setOnDownloadListener(object : DownloadMarketAdapter.OnDownloadListener {
                    override fun onClickDownload(taskManager: TaskManager, bean: AppInfoBean) {
                        try {
                            taskManager.start()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onClickPause(taskManager: TaskManager, bean: AppInfoBean) {
                        taskManager.stop()
                    }

                    override fun onClickDelete(taskManager: TaskManager, bean: AppInfoBean) {
                        taskManager.delete()
                    }
                })
                this.setData(DownloadConstant.getMarketApps())
            }
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}