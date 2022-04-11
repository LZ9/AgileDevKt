package com.lodz.android.agiledevkt.modules.contact

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityContactTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.contacts.bean.ContactsInfoBean
import com.lodz.android.pandora.mvvm.base.activity.BaseVmActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 通讯录测试类
 * @author zhouL
 * @date 2022/3/30
 */
@SuppressLint("NotifyDataSetChanged")
class ContactTestActivity : BaseVmActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ContactTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { ContactViewModel() }

    override fun getViewModel(): ContactViewModel = mViewModel

    private val mBinding: ActivityContactTestBinding by bindingLayout(ActivityContactTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private val hasReadContactsPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.READ_CONTACTS,// 通讯录
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    private val hasWriteContactsPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.WRITE_CONTACTS,// 通讯录
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    /** 适配器 */
    private lateinit var mAdapter: ContactAdapter

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = ContactAdapter(getContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mBinding.contactRv.layoutManager = layoutManager
        mAdapter.onAttachedToRecyclerView(mBinding.contactRv)// 如果使用网格布局请设置此方法
        mBinding.contactRv.setHasFixedSize(true)
        mBinding.contactRv.addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))
        mBinding.contactRv.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        mViewModel.getAllContactData(getContext())
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.deleteAllContactBtn.setOnClickListener {
            showDeleteAllDialog()
        }

        mBinding.addContactBtn.setOnClickListener {
            mViewModel.addContactData(getContext())
        }

        mAdapter.setOnDeleteClickListener { viewHolder, item ->
            showDeleteDialog(item)
        }

        mAdapter.setOnUpdateNoteClickListener { viewHolder, item ->
            item.noteBean.note = "测试"
            mViewModel.updateContactData(getContext(), item.noteBean)
        }
    }

    /** 显示删除所有通讯录弹框 */
    private fun showDeleteAllDialog() {
        AlertDialog.Builder(getContext())
            .setMessage(R.string.contact_delete_all_confirm)
            .setPositiveButton(R.string.contact_delete_ok) { dif, witch ->
                dif.dismiss()
                mViewModel.deleteAllContact(getContext())
            }
            .setNegativeButton(R.string.contact_delete_cancel) { dif, witch ->
                dif.dismiss()
            }
            .create()
            .show()
    }

    /** 显示删除弹框 */
    private fun showDeleteDialog(bean: ContactsInfoBean) {
        AlertDialog.Builder(getContext())
            .setMessage(R.string.contact_delete_confirm)
            .setPositiveButton(R.string.contact_delete_ok) { dif, witch ->
                dif.dismiss()
                mViewModel.deleteContact(getContext(), bean)
            }
            .setNegativeButton(R.string.contact_delete_cancel) { dif, witch ->
                dif.dismiss()
            }
            .create()
            .show()
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mContactList.observe(getLifecycleOwner()){
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun init() {
        showStatusCompleted()
        mViewModel.getAllContactData(getContext())
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermission()//申请权限
        } else {
            init()
        }
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            hasReadContactsPermissions.launch()
            return
        }
        if (!isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            hasWriteContactsPermissions.launch()
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