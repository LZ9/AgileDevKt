package com.lodz.android.agiledevkt.modules.phonefile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityPhoneFileBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.corekt.media.AnkoMedia
import com.lodz.android.corekt.media.deleteFile
import com.lodz.android.pandora.mvvm.base.activity.BaseVmActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel
import kotlin.random.Random

/**
 * 手机文件测试类
 * Created by zhouL on 2018/12/18.
 */
@SuppressLint("NotifyDataSetChanged")
class PhoneFileActivity : BaseVmActivity() {

    private val mViewModel by bindViewModel { PhoneFileViewModel() }

    override fun getViewModel(): PhoneFileViewModel = mViewModel

    private val mBinding: ActivityPhoneFileBinding by bindingLayout(ActivityPhoneFileBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    /** 待删除文件 */
    private var mDeleteFile: DocumentWrapper? = null

    private lateinit var mAdapter: PhoneFileAdapter

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    /** 初始化RecyclerView */
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = PhoneFileAdapter(getContext())
        mBinding.fileRv.layoutManager = layoutManager
        mBinding.fileRv.addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))
        mBinding.fileRv.setHasFixedSize(true)
        mBinding.fileRv.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 点击列表
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            getViewModel().transformBase64(getContext(), item)
        }

        mAdapter.setOnItemLongClickListener { viewHolder, item, position ->
            if (AnkoMedia.isImage(item.documentFile.type) || AnkoMedia.isImageSuffix(item.file.absolutePath)){
                getViewModel().transformBitmap(getContext(), item)
            }
        }

        // 获取相册所有图片路径
        mBinding.allFilePickBtn.setOnClickListener {
            val suffix = mBinding.suffixEdit.text.toString()
            val array = if (suffix.isNotEmpty()) suffix.getArrayBySeparator(",") else arrayOf()
            getViewModel().requestAllFile(getContext(), array)
        }

        mBinding.allImgBtn.setOnClickListener {
            getViewModel().requestAllImage(getContext())
        }

        mBinding.allAudioBtn.setOnClickListener {
            getViewModel().requestAllAudio(getContext())
        }

        mBinding.allVideoBtn.setOnClickListener {
            getViewModel().requestAllVideo(getContext())
        }

        mBinding.groupFolderBtn.setOnClickListener {
            val list = getViewModel().mDocumentList.value
            if (list == null || list.isEmpty()) {
                toastShort(R.string.phone_file_no_file)
                return@setOnClickListener
            }
            getViewModel().groupByFolder(getContext(), list)
        }

        // 随机删除图片
        mBinding.deleteImgBtn.setOnClickListener {
            deleteImg(null)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        getViewModel().mDocumentList.observe(this) {
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()
        }

        getViewModel().mResultStr.observe(this) {
            mBinding.resultTv.text = it
        }
    }

    /** 删除图片的ActivityResult回调 */
    private val mDeleteImgResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            deleteImg(mDeleteFile)
        }
    }

    /** 删除图片 */
    private fun deleteImg(wrapper: DocumentWrapper?) {
        try {
            val photoWrapper = wrapper ?: getRandomDocumentWrapper(getViewModel().mDocumentList.value ?: ArrayList())
            if (photoWrapper == null) {
                toastShort(R.string.phone_file_no_imag)
                return
            }
            mDeleteFile = photoWrapper
            deleteFile(photoWrapper.documentFile.uri)
            toastShort("删除：${photoWrapper.file.name}")
            getViewModel().requestAllImage(getContext())
        } catch (e: Exception) {
            e.printStackTrace()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && e is RecoverableSecurityException) {
                mDeleteImgResult.launch(IntentSenderRequest.Builder(e.userAction.actionIntent.intentSender).build())
            }
        }
    }

    /** 获取随机图片文件 */
    private fun getRandomDocumentWrapper(list: List<DocumentWrapper>): DocumentWrapper? {
        val photoList = ArrayList<DocumentWrapper>()
        for (wrapper in list) {
            if (AnkoMedia.isImage(wrapper.documentFile.type) || AnkoMedia.isImageSuffix(wrapper.file.absolutePath)){
                photoList.add(wrapper)
            }
        }
        if (photoList.size == 0){
            return null
        }
        val index = Random.nextInt(photoList.size)
        return list[index]
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {// 11.0以上的手机要申请存储管理
            if (Environment.isExternalStorageManager()){
                init()
                return
            }
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            mTestActivityResult.launch(intent)
        } else {
            init()
        }
    }

    /** Activity传递回调 */
    val mTestActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            init()
            return@registerForActivityResult
        }
        initData()
    }

    /** 初始化 */
    private fun init() {
        showStatusCompleted()
    }

}