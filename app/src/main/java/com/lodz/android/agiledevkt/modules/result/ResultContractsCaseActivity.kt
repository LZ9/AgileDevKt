package com.lodz.android.agiledevkt.modules.result

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityResultContractsCaseBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.contacts.getContactData
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * ActivityResultContracts的用例
 * @author zhouL
 * @date 2022/3/28
 */
class ResultContractsCaseActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ResultContractsCaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityResultContractsCaseBinding by bindingLayout(ActivityResultContractsCaseBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermission()//申请权限
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.cleanBtn.setOnClickListener {
            cleanLog()
        }

        mBinding.pickContactBtn.setOnClickListener {
            cleanLog()
            mPickContractsResult.launch(null)
        }
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermission()//申请权限
        } else {
            init()
        }
    }

    private fun init() {
        showStatusCompleted()
    }

    val mPickContractsResult = registerForActivityResult(ActivityResultContracts.PickContact()) {
        if (it == null){
            addResultLog("取消通讯录选择或者获取结果为空")
            return@registerForActivityResult
        }
        showContactDetail(it)
    }

    private fun showContactDetail(uri: Uri) {
        val list = getContactData(uri)
        if (list.size == 0){
            addResultLog("未查询到通讯录数据")
            return
        }
        val bean = list[0]
        addResultLog("获取通讯录成功，姓名：${bean.nameBean.name}")
    }

    /** 清空日志 */
    private fun cleanLog() {
        mBinding.resultTv.text = ""
    }

    /** 添加日志 */
    private fun addResultLog(log: String) {
        mBinding.resultTv.text = log.append("\n").append(mBinding.resultTv.text)
    }

    private val hasContactsPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.READ_CONTACTS,// 通讯录
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            hasContactsPermissions.launch()
            return
        }
        init()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    private fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    private fun onDenied() {
        onRequestPermission()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    private fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        goAppDetailSetting()
        showStatusError()
    }
}