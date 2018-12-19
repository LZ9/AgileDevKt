package com.lodz.android.agiledevkt.modules.album

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.album.ImageFolder
import com.lodz.android.corekt.anko.bindView
import java.io.File
import kotlin.random.Random

/**
 * 系统相册测试类
 * Created by zhouL on 2018/12/18.
 */
class AlbumActivity : BaseActivity() {

    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 获取相册所有图片路径 */
    private val mAllPicPathBtn by bindView<MaterialButton>(R.id.all_pic_path_btn)
    /** 获取所有图片的文件夹 */
    private val mAllImgFoldersBtn by bindView<MaterialButton>(R.id.all_img_folders_btn)
    /** 获取图片列表包含的所有图片文件夹 */
    private val mListImgFoldersBtn by bindView<MaterialButton>(R.id.list_img_folders_btn)
    /** 获取总图片的文件夹 */
    private val mTotalImgFolderBtn by bindView<MaterialButton>(R.id.total_img_folder_btn)
    /** 获取指定文件目录下的图片文件夹 */
    private val mImgFolderBtn by bindView<MaterialButton>(R.id.img_folder_btn)
    /** 获取指定图片文件夹里的图片路径列表 */
    private val mListForImgFolderBtn by bindView<MaterialButton>(R.id.list_for_img_folder_btn)
    /** 随机删除图片 */
    private val mDeleteImgBtn by bindView<MaterialButton>(R.id.delete_img_btn)

    override fun getLayoutId(): Int = R.layout.activity_album

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 获取相册所有图片路径
        mAllPicPathBtn.setOnClickListener {
            val list = AlbumUtils.getAllImages(getContext())
            if (list.size == 0) {
                mResultTv.text = getString(R.string.album_no_pic)
                return@setOnClickListener
            }
            var result = getString(R.string.album_count) + list.size.toString() + "\n"
            for (path in list) {
                result += path + "\n\n"
            }
            mResultTv.text = result
        }

        // 获取所有图片的文件夹
        mAllImgFoldersBtn.setOnClickListener {
            val list = AlbumUtils.getAllImageFolders(getContext())
            if (list.size == 0) {
                mResultTv.text = getString(R.string.album_no_pic)
                return@setOnClickListener
            }
            var result = getString(R.string.album_count) + list.size.toString() + "\n"
            for (folder in list) {
                result += imageFolderStr(folder) + "\n\n"
            }
            mResultTv.text = result
        }

        // 获取图片列表包含的所有图片文件夹
        mListImgFoldersBtn.setOnClickListener {
            val list = getPicList()
            var result = "pic " + getString(R.string.album_count) + list.size.toString() + "\n"
            for (path in list) {
                result += path + "\n\n"
            }

            val folders = AlbumUtils.getImageFolders(list)
            result += "\nfolder " + getString(R.string.album_count) + folders.size.toString() + "\n"
            for (folder in folders) {
                result += imageFolderStr(folder) + "\n"
            }
            mResultTv.text = result
        }

        // 获取总图片的文件夹
        mTotalImgFolderBtn.setOnClickListener {
            val folder = AlbumUtils.getTotalImageFolder(getContext())
            val result = getString(R.string.album_count) + "1\n" + imageFolderStr(folder)
            mResultTv.text = result
        }

        // 获取指定文件目录下的图片文件夹
        mImgFolderBtn.setOnClickListener {
            val path = getRandomPath()
            val folder = AlbumUtils.getImageFolder(File(path).parentFile, path)
            mResultTv.text = if (folder == null) "null" else path + "\n\n" + imageFolderStr(folder)
        }

        // 获取指定图片文件夹里的图片路径列表
        mListForImgFolderBtn.setOnClickListener {
            val path = getRandomPath()
            val folder = AlbumUtils.getImageFolder(File(path).parentFile, path)
            var result = "null"
            if (folder == null) {
                mResultTv.text = result
                return@setOnClickListener
            }
            val imgs = AlbumUtils.getImageListOfFolder(getContext(), folder)

            result = path + "\n\n" + imageFolderStr(folder) + "\n\n" + getString(R.string.album_count) + imgs.size.toString() + "\n"
            for (img in imgs) {
                result += img + "\n\n"
            }
            mResultTv.text = result
        }

        // 随机删除图片
        mDeleteImgBtn.setOnClickListener {
            val path = getRandomPath()
            val result = path + "\n" + "is delete : " + AlbumUtils.deleteImage(getContext(), path)
            mResultTv.text = result
        }
    }

    /** 获取随机图片路径列表 */
    private fun getPicList(): List<String> {
        val list = AlbumUtils.getAllImages(getContext())
        if (list.size <= 5) {
            return list
        }
        val results = ArrayList<String>()
        for (i in 0..4) {
            val index = Random.nextInt(list.size)
            results.add(list.get(index))
        }
        return results
    }

    /** 获取随机图片路径 */
    private fun getRandomPath(): String {
        val list = AlbumUtils.getAllImages(getContext())
        val index = Random.nextInt(list.size)
        return list.get(index)
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 获取图片文件夹[folder]信息内容 */
    private fun imageFolderStr(folder: ImageFolder): String =
            "name : ${folder.name}\n" + "coverImgPath : ${folder.coverImgPath}\n" + "dir : ${folder.dir}\n" + "count : ${folder.count}\n" + "isAllPicture : ${folder.isAllPicture()}\n"
}