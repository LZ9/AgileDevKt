package com.lodz.android.pandora.picker.file.pick.phone

import android.content.Context
import android.content.Intent
import com.lodz.android.corekt.anko.startRotateSelf
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.corekt.file.getFileSuffix
import com.lodz.android.corekt.media.*
import com.lodz.android.pandora.R
import com.lodz.android.pandora.databinding.PandoraActivityPickerBinding
import com.lodz.android.pandora.picker.file.PickerBean
import com.lodz.android.pandora.picker.file.PickerManager
import com.lodz.android.pandora.picker.file.dialog.FolderDialog
import com.lodz.android.pandora.picker.file.dialog.FolderItemBean
import com.lodz.android.pandora.picker.file.pick.DataWrapper
import com.lodz.android.pandora.picker.file.pick.any.AnyPickerActivity
import com.lodz.android.pandora.picker.take.TakePhotoManager
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import com.lodz.android.pandora.utils.media.MediaUtils

/**
 * 挑选手机文件
 * @author zhouL
 * @date 2022/5/12
 */
internal class PhonePickerActivity : AnyPickerActivity<DocumentWrapper>() {

    companion object {

        internal fun start(context: Context, pickerBean: PickerBean<DocumentWrapper>, flags: List<Int>?) {
            synchronized(PickerBean::class.java) {
                if (sPickerBean != null) {
                    return
                }
                sPickerBean = pickerBean
                val intent = Intent(context, PhonePickerActivity::class.java)
                flags?.forEach {
                    intent.addFlags(it)
                }
                context.startActivity(intent)
            }
        }
    }

    /** 当前展示的图片文件夹 */
    private var mPdrCurrentDocumentFolder: DocumentFolder? = null
    /** 手机资源数据 */
    private val mPdrPhoneDataList: ArrayList<DocumentWrapper> = arrayListOf()

    override fun initData() {
        super.initData()
        if (mPdrCurrentDocumentFolder == null) {
            mPdrCurrentDocumentFolder = mPdrPhoneDataList.getTotalFolder()
        }
    }

    override fun loadData(bean: PickerBean<DocumentWrapper>, binding: PandoraActivityPickerBinding) {
        CoroutinesWrapper.create(this)
            .request { requestMediaData(bean) }
            .actionPg(context = getContext(), cancelable = false) {
                onSuccess {
                    configAdapterData(it)
                }
            }
    }

    override fun onClickFolderBtn(bean: PickerBean<DocumentWrapper>) {
        CoroutinesWrapper.create(this)
            .request { getFolderList() }
            .actionPg(getContext(), msg = getString(R.string.pandora_picker_folder_loading)) {
                onSuccess { showFolderDialog(it, bean) }
            }
    }

    /** 获取文件夹目录 */
    private fun getFolderList(): List<FolderItemBean> {
        if (mPdrCurrentDocumentFolder == null) {
            mPdrCurrentDocumentFolder = mPdrPhoneDataList.getTotalFolder()
        }
        val list = ArrayList<FolderItemBean>()
        val folders = mPdrPhoneDataList.getAllFileFolder()
        for (folder in folders) {
            val itemBean = FolderItemBean()
            itemBean.documentFolder = folder
            itemBean.isSelected = mPdrCurrentDocumentFolder?.dirPath.equals(folder.dirPath)
            list.add(itemBean)
        }
        return list
    }

    /** 获取文件夹选择弹框 */
    private fun showFolderDialog(list: List<FolderItemBean>, bean: PickerBean<DocumentWrapper>) {
        val dialog = FolderDialog(getContext())
        dialog.setOnImgLoader(bean.imgLoader)
        dialog.setPickerUIConfig(bean.pickerUIConfig)
        dialog.setData(list)
        dialog.setOnCancelListener { dialogInterface ->
            dialogInterface.dismiss()
            mBinding.pdrMoreImg.startRotateSelf(-180, 0, 500, true)
        }
        dialog.setListener { dialogInterface, folderItemBean ->
            dialogInterface.dismiss()
            val documentFolder = folderItemBean.documentFolder ?: return@setListener
            if (mPdrCurrentDocumentFolder?.dirPath.equals(documentFolder.dirPath)) {// 选择了同一个文件夹
                return@setListener
            }
            mPdrCurrentDocumentFolder = documentFolder
            mBinding.pdrFolderNameTv.text = documentFolder.dirName
            configAdapterData(matchSelected(transformDataWrapper(documentFolder.fileList)))
            mBinding.pdrMoreImg.startRotateSelf(-180, 0, 500, true)
        }
        dialog.show()
        mBinding.pdrMoreImg.startRotateSelf(0, -180, 500, true)
    }

    /** 匹配选择列表，将选择状态赋值给新列表 */
    private fun matchSelected(list: List<DataWrapper<DocumentWrapper>>): List<DataWrapper<DocumentWrapper>> {
        list.forEachIndexed { index, documentWrapper ->
            mPdrSelectedList.forEach {
                if (it.data.documentFile.uri == list[index].data.documentFile.uri){
                    list[index].isSelected = true
                }
            }
        }
        return list
    }

    override fun takeCameraPhoto(bean: PickerBean<DocumentWrapper>) {
        TakePhotoManager.create()
            .setPublicDirectoryName(bean.publicDirectoryName)
            .setAuthority(bean.authority)
            .setImmediately(true)
            .setStatusBarColor(getContext(), bean.pickerUIConfig.getStatusBarColor())
            .setNavigationBarColor(getContext(), bean.pickerUIConfig.getNavigationBarColor())
            .setPreviewBgColor(getContext(), bean.pickerUIConfig.getPreviewBgColor())
            .setOnPhotoTakeListener{
                if (it == null){
                    return@setOnPhotoTakeListener
                }
                handleCameraSuccess(bean)
            }
            .build()
            .take(getContext())
    }

    /** 处理拍照成功 */
    private fun handleCameraSuccess(bean: PickerBean<DocumentWrapper>) {
        CoroutinesWrapper.create(this)
            .request { requestMediaData(bean) }
            .actionPg(context = getContext(), cancelable = false) {
                onSuccess {
                    val currentFolder = mPdrCurrentDocumentFolder ?: return@onSuccess
                    val list = mPdrPhoneDataList.getAllFileFolder()
                    for (folder in list) {
                        if (folder.dirPath == currentFolder.dirPath) {
                            configAdapterData(transformDataWrapper(folder.fileList))
                            break
                        }
                    }
                }
            }
    }

    /** 获取手机媒体文件数据 */
    private fun requestMediaData(bean: PickerBean<DocumentWrapper>): ArrayList<DataWrapper<DocumentWrapper>> {
        mPdrPhoneDataList.clear()
        val list = when (bean.pickType) {
            PickerManager.PICK_PHONE_ALBUM -> getMediaImages()
            PickerManager.PICK_PHONE_AUDIO -> getMediaAudios()
            PickerManager.PICK_PHONE_VIDEO -> getMediaVideos()
            PickerManager.PICK_PHONE_SUFFIX -> getAllFiles(*bean.suffixArray)
            PickerManager.PICK_PHONE_ASSEMBLE -> getAssembleData(bean.phoneAssemble)
            else -> arrayListOf()
        }
        list.forEachIndexed { index, documentWrapper ->
            list[index].iconId = MediaUtils.getIconBySuffix(documentWrapper.fileName.getFileSuffix())
        }
        mPdrPhoneDataList.addAll(list)
        return transformDataWrapper(mPdrPhoneDataList)
    }

    /** 组装数据 */
    private fun getAssembleData(types: IntArray): List<DocumentWrapper> {
        val list = ArrayList<DocumentWrapper>()
        for (t in types) {
            list.addAll(
                when (t) {
                    PickerManager.PICK_PHONE_ALBUM -> getMediaImages()
                    PickerManager.PICK_PHONE_AUDIO -> getMediaAudios()
                    PickerManager.PICK_PHONE_VIDEO -> getMediaVideos()
                    else -> arrayListOf()
                }
            )
        }
        return list
    }
}