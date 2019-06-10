package com.lodz.android.pandora.photopicker.picker.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.photopicker.contract.OnImgLoader
import com.lodz.android.pandora.photopicker.picker.PickerUIConfig
import com.lodz.android.pandora.widget.dialog.BaseTopDialog

/**
 * 图片文件弹框
 * Created by zhouL on 2018/12/18.
 */
class ImageFolderDialog(context: Context) : BaseTopDialog(context) {

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 适配器 */
    private lateinit var mAdapter: ImageFolderAdapter

    /** 监听器 */
    private var mListener: ((dialog: DialogInterface, bean: ImageFolderItemBean) -> Unit)? = null
    /** 图片加载器 */
    private var mImgLoader: OnImgLoader<String>? = null
    /** 图片文件夹列表 */
    private var mList: List<ImageFolderItemBean>? = null
    /** UI配置 */
    private var mConfig: PickerUIConfig? = null

    override fun getLayoutId(): Int = R.layout.pandora_dialog_img_folder

    override fun findViews() {
        super.findViews()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = ImageFolderAdapter(context)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    /** 设置图片加载器[imgLoader] */
    fun setOnImgLoader(imgLoader: OnImgLoader<String>?) {
        mImgLoader = imgLoader
    }

    /** 设置图片文件夹列表[list] */
    fun setData(list: List<ImageFolderItemBean>) {
        mList = list
    }

    /** 设置UI配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig) {
        mConfig = config
    }

    /** 设置监听器[listener] */
    fun setListener(listener: (dialog: DialogInterface, bean: ImageFolderItemBean) -> Unit) {
        mListener = listener
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            mListener?.invoke(getDialogInterface(), item)
        }

        mAdapter.setOnImgLoader(mImgLoader)
    }

    override fun initData() {
        super.initData()
        val list = mList
        if (list.isNullOrEmpty()) {
            mRecyclerView.visibility = View.GONE
            return
        }

        configAdapter(list, mConfig ?: PickerUIConfig.createDefault())
    }

    /** 配置适配器 */
    private fun configAdapter(list: List<ImageFolderItemBean>, config: PickerUIConfig) {
        mAdapter.setPickerUIConfig(config)
        mRecyclerView.visibility = View.VISIBLE
        mAdapter.setData(list.toMutableList())
        mRecyclerView.scrollToPosition(getSelectedPosition(list))
        mAdapter.notifyDataSetChanged()
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