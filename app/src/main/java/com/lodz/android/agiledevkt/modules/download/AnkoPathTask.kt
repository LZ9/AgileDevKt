package com.lodz.android.agiledevkt.modules.download

import com.lodz.android.agiledevkt.utils.file.FileManager
import zlc.season.rxdownload4.manager.TaskManager
import zlc.season.rxdownload4.manager.manager
import zlc.season.rxdownload4.task.Task

/**
 * 设置下载地址任务
 * @author zhouL
 * @date 2020/6/14
 */
@JvmOverloads
fun String.pathTaskManager(saveName: String = ""): TaskManager =
    Task(this, saveName = saveName, savePath = FileManager.getDownloadFolderPath()).manager()