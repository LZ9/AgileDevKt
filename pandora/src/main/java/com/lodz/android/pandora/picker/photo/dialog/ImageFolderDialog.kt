package com.lodz.android.pandora.picker.photo.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.photo.PickerUIConfig
import com.lodz.android.pandora.widget.dialog.BaseTopDialog

/**
 * 图片文件弹框
 * Created by zhouL on 2018/12/18.
 */
class ImageFolderDialog(context: Context) : BaseTopDialog(context) {

    /** 列表 */
    private val mPdrRecyclerView by bindView<RecyclerView>(R.id.pdr_img_folder_rv)
    /** 适配器 */
    private lateinit var mPdrAdapter: ImageFolderAdapter

    /** 监听器 */
    private var mPdrListener: ((dialog: DialogInterface, bean: ImageFolderItemBean) -> Unit)? = null
    /** 图片加载器 */
    private var mPdrImgLoader: OnImgLoader<PicInfo>? = null
    /** 图片文件夹列表 */
    private var mPdrList: List<ImageFolderItemBean>? = null
    /** UI配置 */
    private var mPdrConfig: PickerUIConfig? = null

    override fun getLayoutId(): Int = R.layout.pandora_dialog_img_folder

    override fun findViews() {
        super.findViews()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mPdrAdapter = ImageFolderAdapter(context)
        mPdrRecyclerView.layoutManager = layoutManager
        mPdrRecyclerView.setHasFixedSize(true)
        mPdrRecyclerView.adapter = mPdrAdapter
    }

    /** 设置图片加载器[imgLoader] */
    fun setOnImgLoader(imgLoader: OnImgLoader<PicInfo>?) {
        mPdrImgLoader = imgLoader
    }

    /** 设置图片文件夹列表[list] */
    fun setData(list: List<ImageFolderItemBean>) {
        mPdrList = list
    }

    /** 设置UI配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig) {
        mPdrConfig = config
    }

    /** 设置监听器[listener] */
    fun setListener(listener: (dialog: DialogInterface, bean: ImageFolderItemBean) -> Unit) {
        mPdrListener = listener
    }

    override fun setListeners() {
        super.setListeners()
        mPdrAdapter.setOnItemClickListener { viewHolder, item, position ->
            mPdrListener?.invoke(getDialogInterface(), item)
        }

        mPdrAdapter.setOnImgLoader(mPdrImgLoader)
    }

    override fun initData() {
        super.initData()
        val list = mPdrList
        if (list.isNullOrEmpty()) {
            mPdrRecyclerView.visibility = View.GONE
            return
        }

        configAdapter(list, mPdrConfig ?: PickerUIConfig.createDefault())
    }

    /** 配置适配器 */
    private fun configAdapter(list: List<ImageFolderItemBean>, config: PickerUIConfig) {
        mPdrAdapter.setPickerUIConfig(config)
        mPdrRecyclerView.visibility = View.VISIBLE
        mPdrAdapter.setData(list.toMutableList())
        mPdrRecyclerView.scrollToPosition(getSelectedPosition(list))
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 获取选中的位置 */
    private fun getSelectedPosition(list: List<ImageFolderItemBean>): Int {
        list.forEachIndexed { index, folder ->
            if (folder.isSelected) {
                return index
            }
        }
        return 0
    }
}