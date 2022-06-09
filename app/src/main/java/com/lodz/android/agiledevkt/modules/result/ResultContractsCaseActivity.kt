package com.lodz.android.agiledevkt.modules.result

import android.Manifest
import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.agiledevkt.BuildConfig
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityResultContractsCaseBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.contacts.getContactData
import com.lodz.android.corekt.media.*
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

        mBinding.pickContactBtn.setOnClickListener {
            reset()
            mPickContractsResult.launch(null)
        }

        mBinding.takePictureBtn.setOnClickListener {
            reset()
            mPair = insertImageForPair(
                DateUtils.getCurrentFormatString(DateUtils.TYPE_3).append(".jpg"),
                BuildConfig.FILE_AUTHORITY,
                Environment.DIRECTORY_DCIM
            )
            mTakePictureResult.launch(mPair?.first)
        }

        mBinding.takePicturePreviewBtn.setOnClickListener {
            reset()
            mTakePicturePreviewResult.launch(null)
        }

        mBinding.openDocumentTreeBtn.setOnClickListener {
            reset()
            mOpenDocumentTreeResult.launch(null)
        }

        mBinding.getContentBtn.setOnClickListener {
            reset()
            showMimeTypeDialog(false) {
                if (it.isNotEmpty()){
                    mGetContentResult.launch(it[0])
                }
            }
        }

        mBinding.openDocumentBtn.setOnClickListener {
            reset()
            showMimeTypeDialog(true) {
                mOpenDocumentResult.launch(it)
            }
        }

        mBinding.getMultipleContentsBtn.setOnClickListener {
            reset()
            showMimeTypeDialog(false) {
                if (it.isNotEmpty()){
                    mGetMultipleContentsResult.launch(it[0])
                }
            }
        }

        mBinding.openMultipleDocumentsBtn.setOnClickListener {
            reset()
            showMimeTypeDialog(true) {
                mOpenMultipleDocumentsResult.launch(it)
            }
        }

        mBinding.captureVideoBtn.setOnClickListener {
            reset()
            val path = FileManager.getContentFolderPath().append("${DateUtils.getCurrentFormatString(DateUtils.TYPE_3)}.mp4")
            addResultLog("视频存储路径：$path")
            val file = File(path)
            mPair = insertVideoForPair(file.name, BuildConfig.FILE_AUTHORITY)
            mCaptureVideoResult.launch(mPair?.first)
        }
    }

    /** 显示文件类型弹框 */
    private fun showMimeTypeDialog(isSelected: Boolean, block: (Array<String>) -> Unit) {
        val dialog = MimeTypeDialog(getContext(), isSelected)
        dialog.setOnSelectedListener { dif, mineType ->
            addResultLog("文件类型：${mineType.contentToString()}")
            dif.dismiss()
            block(mineType)
        }
        dialog.show()
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

    private var mPair: Pair<Uri, File>? = null

    /** 拍照回调 */
    val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        val uri = mPair?.first
        if (!it || uri == null) {
            addResultLog("取消拍照")
            return@registerForActivityResult
        }
        mPair?.second?.notifyScanImage(getContext())
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


    /** 选择文件目录路径回调 */
    val mOpenDocumentTreeResult = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        if (it == null){
            addResultLog("取消选择")
            return@registerForActivityResult
        }
        addResultLog("选择目录路径：${it}")
        addResultLog("路径名：${DocumentFile.fromTreeUri(getContext(), it)?.name}")
    }

    /** 单类型选择单文件回调 */
    val mGetContentResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null){
            addResultLog("取消选择")
            return@registerForActivityResult
        }
        addResultLog("选择文件路径：${it}")
        addResultLog("文件名：${getUriName(it)}")
    }

    /** 多类型选择单文件回调 */
    val mOpenDocumentResult = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it == null){
            addResultLog("取消选择")
            return@registerForActivityResult
        }
        addResultLog("选择文件路径：${it}")
        addResultLog("文件名：${getUriName(it)}")
    }

    /** 单类型选择多文件回调 */
    val mGetMultipleContentsResult = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        if (it == null){
            addResultLog("取消选择")
            return@registerForActivityResult
        }
        addResultLog("选择文件路径：${it}")
        for (uri in it) {
            addResultLog("文件名：${getUriName(uri)}")
        }
    }

    /** 多类型选择多文件回调 */
    val mOpenMultipleDocumentsResult = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) {
        if (it == null){
            addResultLog("取消选择")
            return@registerForActivityResult
        }
        addResultLog("选择文件路径：${it}")
        for (uri in it) {
            addResultLog("文件名：${getUriName(uri)}")
        }
    }

    /** 拍摄视频回调 */
    val mCaptureVideoResult = registerForActivityResult(ActivityResultContracts.CaptureVideo()) {
        val uri = mPair?.first
        if (!it || uri == null) {
            addResultLog("取消拍摄")
            return@registerForActivityResult
        }
        mPair?.second?.notifyScanVideo(getContext())
        mBinding.resultImg.visibility = View.VISIBLE
        ImageLoader.create(this).loadUri(uri).into(mBinding.resultImg)
        addResultLog("选择文件路径：${uri}")
        addResultLog("文件名：${getUriName(uri)}")
    }

    /** 重置 */
    private fun reset() {
        mPair = null
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

    /** 获取Uri的文件名称 */
    private fun getUriName(uri: Uri): String {
        val file = DocumentFile.fromSingleUri(getContext(), uri)
        return file?.name ?: "获取失败"
    }
}