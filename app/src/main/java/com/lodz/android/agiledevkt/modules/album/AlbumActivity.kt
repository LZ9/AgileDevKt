package com.lodz.android.agiledevkt.modules.album

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityAlbumBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.album.ImageFolder
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlin.random.Random

/**
 * 系统相册测试类
 * Created by zhouL on 2018/12/18.
 */
class AlbumActivity : BaseActivity() {

    /** 待删除图片 */
    private var mDeleteInfo: PicInfo? = null

    private val mBinding: ActivityAlbumBinding by bindingLayout(ActivityAlbumBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 获取相册所有图片路径
        mBinding.allPicPathBtn.setOnClickListener {
            val list = AlbumUtils.getAllImages(getContext())
            if (list.isEmpty()) {
                mBinding.resultTv.text = getString(R.string.album_no_pic)
                return@setOnClickListener
            }
            var result = getString(R.string.album_count) + list.size.toString() + "\n"
            for (info in list) {
                result += info.toString() + "\n\n"
            }
            mBinding.resultTv.text = result
        }

        // 获取所有图片的文件夹
        mBinding.allImgFoldersBtn.setOnClickListener {
            val list = AlbumUtils.getAllImageFolders(getContext())
            if (list.isEmpty()) {
                mBinding.resultTv.text = getString(R.string.album_no_pic)
                return@setOnClickListener
            }
            var result = getString(R.string.album_count) + list.size.toString() + "\n"
            for (folder in list) {
                result += imageFolderStr(folder) + "\n\n"
            }
            mBinding.resultTv.text = result
        }

        // 获取图片列表包含的所有图片文件夹
        mBinding.listImgFoldersBtn.setOnClickListener {
            val list = getPicList()
            var result = "pic " + getString(R.string.album_count) + list.size.toString() + "\n"
            for (info in list) {
                result += info.toString() + "\n\n"
            }

            val folders = AlbumUtils.getImageFolders(list)
            result += "\nfolder " + getString(R.string.album_count) + folders.size.toString() + "\n"
            for (folder in folders) {
                result += imageFolderStr(folder) + "\n"
            }
            mBinding.resultTv.text = result
        }

        // 获取总图片的文件夹
        mBinding.totalImgFolderBtn.setOnClickListener {
            val folder = AlbumUtils.getTotalImageFolder(getContext())
            val result = getString(R.string.album_count) + "1\n" + imageFolderStr(folder)
            mBinding.resultTv.text = result
        }

        // 获取指定文件目录下的图片文件夹
        mBinding.imgFolderBtn.setOnClickListener {
            val info = getRandomPath()
            val folder = AlbumUtils.getImageFolder(getContext(), info)
            mBinding.resultTv.text = if (folder == null) "null" else info.toString() + "\n\n" + imageFolderStr(folder)
        }

        // 获取指定图片文件夹里的图片路径列表
        mBinding.listForImgFolderBtn.setOnClickListener {
            val info = getRandomPath()
            val folder = AlbumUtils.getImageFolder(getContext(), info)
            var result = "null"
            if (folder == null) {
                mBinding.resultTv.text = result
                return@setOnClickListener
            }
            val imgs = folder.picList
            if (imgs.size == 0){
                mBinding.resultTv.text = "imgs is null"
                return@setOnClickListener
            }

            result = info.toString() + "\n\n" + imageFolderStr(folder) + "\n\n" + getString(R.string.album_count) + imgs.size.toString() + "\n"
            for (img in imgs) {
                result += img.toString() + "\n\n"
            }
            mBinding.resultTv.text = result
        }

        // 随机删除图片
        mBinding.deleteImgBtn.setOnClickListener {
            deleteImg(null)
        }
    }

    private fun deleteImg(picInfo: PicInfo?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val info = picInfo ?: getRandomPath()
                mDeleteInfo = info
                val result = info.toString() + "\n" + "is delete : " + AlbumUtils.deleteImageCompat(getContext(), info)
                mBinding.resultTv.text = result
            } catch (e: RecoverableSecurityException) {
                e.printStackTrace()
                startIntentSenderForResult(
                    e.getUserAction().getActionIntent().getIntentSender(), Build.VERSION.SDK_INT, null, 0, 0, 0
                )
            }
            return
        }

        val info = getRandomPath()
        val result = info.toString() + "\n" + "is delete : " + AlbumUtils.deleteImage(getContext(), info.path)
        mBinding.resultTv.text = result
    }

    /** 获取随机图片路径列表 */
    private fun getPicList(): List<PicInfo> {
        val list = AlbumUtils.getAllImages(getContext())
        if (list.size <= 5) {
            return list
        }
        val results = ArrayList<PicInfo>()
        for (i in 0..4) {
            val index = Random.nextInt(list.size)
            results.add(list[index])
        }
        return results
    }

    /** 获取随机图片路径 */
    private fun getRandomPath(): PicInfo {
        val list = AlbumUtils.getAllImages(getContext())
        val index = Random.nextInt(list.size)
        return list[index]
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 获取图片文件夹[folder]信息内容 */
    private fun imageFolderStr(folder: ImageFolder): String =
            "name : ${folder.name}\n" + "coverImgPath : ${folder.getCoverPicInfo()}\n" + "dir : ${folder.dir}\n" + "count : ${folder.getCount()}\n" + "isAllPicture : ${folder.isAllPicture()}\n"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Build.VERSION.SDK_INT && resultCode == Activity.RESULT_OK){
            deleteImg(mDeleteInfo)
        }
    }
}