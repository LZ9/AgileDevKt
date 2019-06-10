package com.lodz.android.pandora.widget.ninegrid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.lodz.android.corekt.anko.toArrayList
import com.lodz.android.pandora.photopicker.picker.PickerManager
import com.lodz.android.pandora.photopicker.picker.PickerUIConfig
import com.lodz.android.pandora.photopicker.preview.PreviewManager


/**
 * 简单的九宫格实现
 * Created by zhouL on 2018/12/26.
 */
class SimpleNineGridView<V : View> : NineGridView {

    /** 接口  */
    private var mListener: OnSimpleNineGridViewListener<V>? = null
    /** 照片保存地址  */
    private var mCameraSavePath = ""
    /** 7.0的FileProvider名字  */
    private var mAuthority = ""
    /** 是否需要拍照  */
    private var isNeedCamera = true
    /** 是否需要预览Item  */
    private var isNeedItemPreview = true
    /** 选择器UI配置  */
    private var mConfig = PickerUIConfig.createDefault()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setListener(object : OnNineGridViewListener {
            override fun onAddPic(addCount: Int) {
                val listener = mListener ?: return
                PickerManager.create<V>()
                        .setImgLoader { context, source, imageView ->
                            mListener?.onDisplayPickerImg(context, source, imageView)
                        }
                        .setImageView(listener.createImageView())
                        .setOnPhotoPickerListener { photos ->
                            addDatas(photos.toArrayList())
                        }
                        .setMaxCount(addCount)
                        .setNeedCamera(isNeedCamera)
                        .setNeedItemPreview(isNeedItemPreview)
                        .setPickerUIConfig(mConfig)
                        .setCameraSavePath(mCameraSavePath)
                        .setAuthority(mAuthority)
                        .build()
                        .open(context)
            }

            override fun onDisplayImg(context: Context, data: String, imageView: ImageView) {
                mListener?.onDisplayNineGridImg(context, data, imageView)
            }

            override fun onDeletePic(data: String, position: Int) {
                removeDatas(position)
            }

            override fun onClickPic(data: String, position: Int) {
                val listener = mListener ?: return
                PreviewManager.create<V, String>()
                        .setPosition(position)
                        .setBackgroundColor(android.R.color.black)
                        .setStatusBarColor(android.R.color.black)
                        .setNavigationBarColor(android.R.color.black)
                        .setPagerTextColor(android.R.color.white)
                        .setPagerTextSize(14)
                        .setShowPagerText(true)
                        .setImageView(listener.createImageView())
                        .build(getPicData())
                        .open(context)
            }
        })
    }

    /** 配置数据，图片保存地址[savePath]，FileProvider名字[authority] */
    fun config(savePath: String, authority: String) {
        mCameraSavePath = savePath
        mAuthority = authority
    }

    /** 设置监听器[listener] */
    fun setOnSimpleNineGridViewListener(listener: OnSimpleNineGridViewListener<V>) {
        mListener = listener
    }

    /** 设置是否需要拍照[isNeed] */
    fun setNeedCamera(isNeed: Boolean) {
        isNeedCamera = isNeed
    }

    /** 设置是否需要预览Item[isNeed] */
    fun setNeedItemPreview(isNeed: Boolean) {
        isNeedItemPreview = isNeed
    }

    /** 设置选择器UI配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig) {
        this.mConfig = config
    }

    override fun addData(data: ArrayList<String>) {}

    override fun removeData(position: Int) {}

    override fun setOnNineGridViewListener(listener: OnNineGridViewListener) {}

    private fun addDatas(data: ArrayList<String>) {
        super.addData(data)
    }

    private fun removeDatas(position: Int) {
        super.removeData(position)
    }

    private fun setListener(listener: OnNineGridViewListener) {
        super.setOnNineGridViewListener(listener)
    }
}