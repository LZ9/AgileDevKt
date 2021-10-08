package com.lodz.android.agiledevkt.modules.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMsgDetailBinding
import com.lodz.android.corekt.anko.isTopAndBottomActivityTheSame
import com.lodz.android.corekt.anko.restartApp
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import java.util.*

/**
 * 消息详情
 * @author zhouL
 * @date 2019/7/16
 */
class MsgDetailActivity : BaseActivity() {

    companion object {

        private const val EXTRA_MSG_DATA = "extra_msg_data"

        /** 获取PendingIntent来启动，[context]上下文，[data]数据 */
        fun startPendingIntent(context: Context, data: String): PendingIntent {
            val intent = Intent(context, MsgDetailActivity::class.java)
            intent.putExtra(EXTRA_MSG_DATA, data)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT)
            }
        }
    }

    /** 数据 */
    private var mData = ""

    private val mBinding: ActivityMsgDetailBinding by bindingLayout(ActivityMsgDetailBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.msg_detail_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        showStatusLoading()
        handleIntent(intent)
    }

    /** 处理Intent */
    private fun handleIntent(intent: Intent?) {
        if (intent == null){
            showStatusNoData()
            return
        }
        val data: String? = intent.getStringExtra(EXTRA_MSG_DATA)
        if (data.isNullOrEmpty()){
            showStatusNoData()
            return
        }
        mData = data
        mBinding.dataTv.text = mData
        showStatusCompleted()
    }

    override fun finish() {
        if(isTopAndBottomActivityTheSame()){
            // 如果APP未打开直接进入详情页 则关闭时重启APP
            restartApp()
        }
        super.finish()
    }
}