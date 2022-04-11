package com.lodz.android.agiledevkt.modules.result

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityResultContractsCaseBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.contacts.getContactData
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest
import java.io.File

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
            reset()
        }

        mBinding.jumpActivityBtn.setOnClickListener {
            reset()
            mTestActivityResult.launch(Intent(getContext(), TestResultActivity::class.java))
        }

        mBinding.jumpFragmentBtn.setOnClickListener {
            toastShort("开发中")
        }

        mBinding.pickContactBtn.setOnClickListener {
            reset()
            mPickContractsResult.launch(null)
        }

        mBinding.takePictureBtn.setOnClickListener {
            reset()
            mPictureUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val file = File(DateUtils.getCurrentFormatString(DateUtils.TYPE_3).append(".jpg"))
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                values.put(MediaStore.Images.Media.TITLE, file.name)
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                FileProvider.getUriForFile(
                    getContext(),
                    "com.lodz.android.agiledevkt.fileprovider",
                    File(FileManager.getContentFolderPath().append(DateUtils.getCurrentFormatString(DateUtils.TYPE_3)).append(".jpg"))
                )
            }
            mTakePictureResult.launch(mPictureUri)
        }

        mBinding.takePicturePreviewBtn.setOnClickListener {
            reset()
            mTakePicturePreviewResult.launch(null)
        }

        mBinding.openDocumentTreeBtn.setOnClickListener {
            reset()
            mOpenDocumentTreeResult.launch(null)
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

    /** 选择通讯录回调 */
    val mPickContractsResult = registerForActivityResult(ActivityResultContracts.PickContact()) {
        if (it == null) {
            addResultLog("取消通讯录选择或者获取结果为空")
            return@registerForActivityResult
        }
        val list = getContactData(it)
        if (list.size == 0) {
            addResultLog("未查询到通讯录数据")
            return@registerForActivityResult
        }
        val bean = list[0]
        addResultLog("获取通讯录成功，姓名：${bean.nameBean.name}")
    }

    /** Activity传递回调 */
    val mTestActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val str = it.data?.getStringExtra(Activity.RESULT_OK.toString()) ?: "未获取传递数据"
            addResultLog(str)
            return@registerForActivityResult
        }
        addResultLog("未获取传递数据")
    }

    private var mPictureUri: Uri? = null

    /** 拍照回调 */
    val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        val uri = mPictureUri
        if (!it || uri == null) {
            addResultLog("取消拍照")
            return@registerForActivityResult
        }
        mBinding.resultImg.visibility = View.VISIBLE
        ImageLoader.create(this).loadUri(uri).into(mBinding.resultImg)
        addResultLog(uri.toString())
    }

    /** 拍预览图回调 */
    val mTakePicturePreviewResult = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it == null){
            addResultLog("取消拍照")
            return@registerForActivityResult
        }
        mBinding.resultImg.visibility = View.VISIBLE
        ImageLoader.create(this).loadBitmap(it).into(mBinding.resultImg)
    }


    /** 选择一个文件目录回调 */
    val mOpenDocumentTreeResult = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        if (it == null){
            addResultLog("取消选择")
            return@registerForActivityResult
        }
        addResultLog("选择目录路径：${it}")
    }


    /** 重置 */
    private fun reset() {
        mPictureUri = null
        mBinding.resultTv.text = ""
        mBinding.resultImg.visibility = View.GONE
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

    private val hasCameraPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.CAMERA,// 拍照
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
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            hasCameraPermissions.launch()
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