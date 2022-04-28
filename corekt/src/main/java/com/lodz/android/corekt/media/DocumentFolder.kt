package com.lodz.android.corekt.media

import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.file.DocumentWrapper

/**
 * 文件夹实体
 * Created by zhouL on 2018/11/5.
 */
class DocumentFolder {

    /** 文件夹路径 */
    var dirPath = ""

    /** 文件夹的名称 */
    var dirName = ""

    /** 文件夹里包含的文件 */
    var fileList: ArrayList<DocumentWrapper> = ArrayList()

    /** 文件夹封面文件 */
    var coverDocument: DocumentWrapper? = null

    /** 是否所有文件目录 */
    fun isAllFileDir(): Boolean = dirPath.isEmpty()

    /** 添加文件[document] */
    internal fun addDocument(document: DocumentWrapper) {
        fileList.add(document)
    }
    /** 添加文件[documents] */
    internal fun addDocument(documents: List<DocumentWrapper>) {
        fileList.addAll(documents)
    }

    /** 文件夹文件的数量 */
    fun getCount(): Int = fileList.getSize()

}