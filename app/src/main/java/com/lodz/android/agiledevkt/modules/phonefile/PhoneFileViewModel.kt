package com.lodz.android.agiledevkt.modules.phonefile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.toArrayList
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.corekt.file.toBase64
import com.lodz.android.corekt.media.*
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper

/**
 * 手机文件ViewModel
 * @author zhouL
 * @date 2022/4/24
 */
class PhoneFileViewModel : BaseViewModel() {

    var mDocumentList = MutableLiveData<ArrayList<DocumentWrapper>>()

    var mResultStr = MutableLiveData<String>()


    /** 文件转换base64 */
    fun transformBase64(context: Context, bean: DocumentWrapper) {
        // base64转换结果测试：http://www.ec95.com/
        CoroutinesWrapper.create(this)
            .request { bean.file.toBase64() }
            .actionPg(context){
                onSuccess {
                    val tips = if (it.isEmpty()) R.string.phone_file_transform_fail else R.string.phone_file_transform_success
                    toastShort(tips)
                }
            }
    }

    /** 图片转Bitmap */
    fun transformBitmap(context: Context, bean: DocumentWrapper) {
        CoroutinesWrapper.create(this)
            .request { BitmapUtils.uriToBitmap(context, bean.documentFile.uri) }
            .actionPg(context){
                onSuccess {
                    val tips = if (it == null) R.string.phone_file_transform_fail else R.string.phone_file_transform_success
                    toastShort(tips)
                }
            }
    }

    /** 请求手机内所有文件数据 */
    fun requestAllFile(context: Context, suffix: Array<out String>) {
        CoroutinesWrapper.create(this)
            .request { context.getAllFiles(*suffix) }
            .actionPg(context){
                onSuccess {
                    mResultStr.value =  context.getString(R.string.phone_file_count) + it.size.toString()
                    mDocumentList.value = it.toArrayList()
                    if (it.isEmpty()){
                        toastShort(R.string.phone_file_no_file)
                    }
                }
            }
    }

    /** 请求手机图片数据 */
    fun requestAllImage(context: Context) {
        CoroutinesWrapper.create(this)
            .request { context.getMediaImages() }
            .actionPg(context){
                onSuccess {
                    mResultStr.value =  context.getString(R.string.phone_file_count) + it.size.toString()
                    mDocumentList.value = it.toArrayList()
                    if (it.isEmpty()){
                        toastShort(R.string.phone_file_no_file)
                    }
                }
            }
    }

    /** 请求手机音频数据 */
    fun requestAllAudio(context: Context) {
        CoroutinesWrapper.create(this)
            .request { context.getMediaAudios() }
            .actionPg(context){
                onSuccess {
                    mResultStr.value =  context.getString(R.string.phone_file_count) + it.size.toString()
                    mDocumentList.value = it.toArrayList()
                    if (it.isEmpty()){
                        toastShort(R.string.phone_file_no_file)
                    }
                }
            }
    }

    /** 请求手机视频数据 */
    fun requestAllVideo(context: Context) {
        CoroutinesWrapper.create(this)
            .request { context.getMediaVideos() }
            .actionPg(context){
                onSuccess {
                    mResultStr.value = context.getString(R.string.phone_file_count) + it.size.toString()
                    mDocumentList.value = it.toArrayList()
                    if (it.isEmpty()){
                        toastShort(R.string.phone_file_no_file)
                    }
                }
            }
    }

    /** 对列表数据分组 */
    fun groupByFolder(context: Context, list: ArrayList<DocumentWrapper>) {
        CoroutinesWrapper.create(this)
            .request {
                val folders = list.groupByFolder()
                var log = context.getString(R.string.phone_file_count).append(folders.size).append("\n")
                folders.forEachIndexed { index, folder ->
                    log = log.append(index + 1).append("    ").append(folder.dirPath).append("\n")
                }
                log
            }
            .actionPg(context){
                onSuccess {
                    mResultStr.value = it
                }
            }
    }
}