package com.lodz.android.pandora.picker.file.pick.phone

import android.content.Context
import android.content.Intent
import android.view.View
import com.lodz.android.corekt.anko.startRotateSelf
import com.lodz.android.corekt.file.DocumentWrapper
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

/**
 * 挑选手机文件
 * @author zhouL
 * @date 2022/5/12
 */
internal class PhonePickerActivity<V : View> : AnyPickerActivity<V, DocumentWrapper>() {

    companion object {

        internal fun <V : View> start(context: Context, pickerBean: PickerBean<V, DocumentWrapper>, flags: List<Int>?) {
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

    private val mPdrPhoneDataList: ArrayList<DocumentWrapper> = arrayListOf()

    override fun initData() {
        super.initData()
        if (mPdrCurrentDocumentFolder == null) {
            mPdrCurrentDocumentFolder = mPdrPhoneDataList.getTotalFolder()
        }
    }

    override fun loadData(bean: PickerBean<V, DocumentWrapper>, binding: PandoraActivityPickerBinding) {
        CoroutinesWrapper.create(this)
            .request { requestMediaData(bean) }
            .actionPg(context = getContext(), cancelable = false) {
                onSuccess {
                    configAdapterData(it)
                }
            }
    }

    override fun onClickFolderBtn(bean: PickerBean<V, DocumentWrapper>) {
        CoroutinesWrapper.create(this)
            .request {
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
                list
            }
            .actionPg(getContext(), msg = getString(R.string.pandora_picker_folder_loading)){
                onSuccess {
                    val dialog = FolderDialog(getContext())
                    dialog.setOnImgLoader(bean.imgLoader)
                    dialog.setPickerUIConfig(bean.pickerUIConfig)
                    dialog.setData(it)
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
                        configAdapterData(transformDataWrapper(documentFolder.fileList))
                        mBinding.pdrMoreImg.startRotateSelf(-180, 0, 500, true)
                    }
                    dialog.show()
                    mBinding.pdrMoreImg.startRotateSelf(0, -180, 500, true)
                }
            }
    }


    override fun handlePreview(datas: ArrayList<DataWrapper<DocumentWrapper>>) {
        super.handlePreview(datas)



    }

    override fun takeCameraPhoto(bean: PickerBean<V, DocumentWrapper>) {
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
    private fun handleCameraSuccess(bean: PickerBean<V, DocumentWrapper>) {
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

    private fun requestMediaData(bean: PickerBean<V, DocumentWrapper>): ArrayList<DataWrapper<DocumentWrapper>> {
        mPdrPhoneDataList.clear()
        mPdrPhoneDataList.addAll(
            when (bean.pickType) {
                PickerManager.PICK_PHONE_ALBUM -> getMediaImages()
                PickerManager.PICK_PHONE_AUDIO -> getMediaAudios()
                PickerManager.PICK_PHONE_VIDEO -> getMediaVideos()
                PickerManager.PICK_PHONE_SUFFIX -> getAllFiles(*bean.suffixArray)
                PickerManager.PICK_PHONE_ASSEMBLE -> getAssembleData(bean.phoneAssemble)
                else -> arrayListOf()
            }
        )
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