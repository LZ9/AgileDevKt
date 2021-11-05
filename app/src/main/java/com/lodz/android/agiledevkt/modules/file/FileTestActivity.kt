package com.lodz.android.agiledevkt.modules.file

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityFileTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Observable
import java.io.File

/**
 * 文件测试类
 * Created by zhouL on 2018/6/28.
 */
class FileTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FileTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 根目录地址 */
    private val ROOT_PATH = FileManager.getCacheFolderPath()
    /** 测试文件名称 */
    private val TEST_FILE_NAME = "test.txt"
    /** 新增文件路径 */
    private val NEW_FILE_PATH = ROOT_PATH + TEST_FILE_NAME
    /** 新增文件夹路径 */
    private val NEW_FOLDER_PATH = ROOT_PATH + "test" + File.separator + "12321"
    /** 保存文件夹路径 */
    private val SAVE_PATH = ROOT_PATH + "test" + File.separator + "dasdqweq"
    /** 视频文件名称 */
    private val VIDEO_FILE_NAME = "video.mp4"

    private val mBinding: ActivityFileTestBinding by bindingLayout(ActivityFileTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 创建一个文件
        mBinding.createNewFileBtn.setOnClickListener {
            val isSuccess = FileUtils.createNewFile(NEW_FILE_PATH)
            toastShort("创建 : $isSuccess")
        }

        // 创建一个文件夹
        mBinding.createDirectoryBtn.setOnClickListener {
            val isSuccess = FileUtils.createFolder(NEW_FOLDER_PATH)
            toastShort("创建 : $isSuccess")
        }

        // 文件（文件夹）是否存在
        mBinding.fileExistsBtn.setOnClickListener {
            val file = FileUtils.create(NEW_FILE_PATH)
            toastShort(if (FileUtils.isFileExists(file)) "文件存在" else "文件不存在")
        }

        // 重命名文件（文件夹）
        mBinding.fileRenameBtn.setOnClickListener {
            val isSuccess = FileUtils.renameFile(FileUtils.create(NEW_FILE_PATH), "asdsada.txt")
            toastShort("修改 : $isSuccess")
        }

        // 删除jpg后缀文件
        mBinding.fileDeleteSuffixJpgBtn.setOnClickListener {
            FileUtils.deleteFileWithSuffix(ROOT_PATH, "jpg")
            toastShort("删除完成")
        }

        // 移动文件
        mBinding.moveFileBtn.setOnClickListener {
            val isSuccess = FileUtils.moveFile(ROOT_PATH, NEW_FOLDER_PATH, TEST_FILE_NAME)
            toastShort("移动 : $isSuccess")
        }

        // 复制文件
        mBinding.copyFileBtn.setOnClickListener {
            val isSuccess = FileUtils.copyFile(ROOT_PATH, NEW_FOLDER_PATH, TEST_FILE_NAME)
            toastShort("复制 : $isSuccess")
        }

        // 获取路径下的文件总大小
        mBinding.getFileLength.setOnClickListener {
            val size = FileUtils.getFileTotalLengthUnit(NEW_FILE_PATH)
            toastShort("大小 : $size")
        }

        // 删除路径下文件
        mBinding.deleteFileBtn.setOnClickListener {
            FileUtils.delFile(ROOT_PATH)
            toastShort("删除完成")
        }

        // 将文件保存为byte数组并保存到其他路径
        mBinding.saveByBytesBtn.setOnClickListener {
            val bytes: ByteArray? = FileUtils.fileToByte(NEW_FILE_PATH)
            if (bytes == null){
                toastShort("转换失败")
                return@setOnClickListener
            }
            val isSuccess = FileUtils.byteToFile(bytes, SAVE_PATH, "17283971234654.txt")
            toastShort(isSuccess.then { "转换完成" } ?: "转换失败")
        }

        // bitmap转文件
        mBinding.saveBitmapBtn.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pandora_ic_search)
            FileUtils.bitmapToPath(bitmap, SAVE_PATH, "12sdaww","png", 100)
            toastShort("保存完成")
        }

        // 文件转Base64
        mBinding.fileToBase64Btn.setOnClickListener {
            val videoFile = File(FileManager.getContentFolderPath().append(VIDEO_FILE_NAME))
            if (!videoFile.exists()) {
                toastShort(R.string.file_video_no_found)
                return@setOnClickListener
            }
            fileToBase64(videoFile)
        }

        // 读取文件内容
        mBinding.readContentBtn.setOnClickListener {
            Observable.just(NEW_FILE_PATH)
                .map {
                    FileUtils.fileToString(NEW_FILE_PATH) ?: "转换失败"
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(BaseObserver.action(next = {
                    toastShort(it)
                }))
        }

        // 将文本写入文件末尾
        mBinding.writeContentEndBtn.setOnClickListener {
            Observable.just(NEW_FILE_PATH)
                .map {
                    val file = File(it)
                    if (!file.exists()){
                        FileUtils.createNewFile(it)
                    }
                    return@map file
                }
                .map {
                    val content = DateUtils.getCurrentFormatString(DateUtils.TYPE_2).append(" : ").append("测试").append("\n")
                    FileUtils.writeFileToEnd(it, content)
                    it
                }
                .map {
                    FileUtils.fileToString(it.absolutePath) ?: "写入失败"
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(
                    ProgressObserver.action(
                        context = getContext(),
                        msg = "写入中",
                        cancelable = false,
                        canceledOnTouchOutside = false,
                        next = {
                            toastShort(it)
                        },
                        error = { e, isNetwork ->
                            toastShort("写入失败：${e.message}")
                        })
                )
        }

        // 将文本写入文件开头
        mBinding.writeContentStartBtn.setOnClickListener {
            Observable.just(NEW_FILE_PATH)
                .map {
                    val file = File(it)
                    if (!file.exists()){
                        FileUtils.createNewFile(it)
                    }
                    return@map file
                }
                .map {
                    val content = DateUtils.getCurrentFormatString(DateUtils.TYPE_23).append(" : ").append("测试").append("\n")
                    FileUtils.writeFileToStart(it, content)
                    it
                }
                .map {
                    FileUtils.fileToString(it.absolutePath) ?: "写入失败"
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(
                    ProgressObserver.action(
                        context = getContext(),
                        msg = "写入中",
                        cancelable = false,
                        canceledOnTouchOutside = false,
                        next = {
                            toastShort(it)
                        },
                        error = { e, isNetwork ->
                            toastShort("写入失败：${e.message}")
                        })
                )
        }
    }


    /** 文件转base64 */
    private fun fileToBase64(file: File) {
        Observable.just(file)
            .map {
                FileUtils.fileToBase64(it)
            }
            .map {
                if (it.isEmpty()){
                    throw TypeCastException("base64 is null")
                }
                FileUtils.Base64ToFile(it, FileManager.getContentFolderPath(), "111".append(VIDEO_FILE_NAME)) ?: throw NullPointerException("file is null")
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(
                ProgressObserver.action(
                    context = getContext(),
                    msg = "转码中",
                    cancelable = false,
                    canceledOnTouchOutside = false,
                    next = {
                        if (it != null) toastShort("转码成功") else toastShort("转码失败")
                    },
                    error = { e, isNetwork ->
                        toastShort("转码失败：${e.message}")
                    })
            )
    }

    override fun initData() {
        super.initData()
        mBinding.rootPathTv.text = ("根目录地址：$ROOT_PATH")
        showStatusCompleted()
    }


}