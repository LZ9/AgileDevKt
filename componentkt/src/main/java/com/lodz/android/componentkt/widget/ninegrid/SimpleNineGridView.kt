package com.lodz.android.componentkt.widget.ninegrid

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.contract.picker.OnPhotoPickerListener
import com.lodz.android.componentkt.photopicker.contract.preview.PreviewController
import com.lodz.android.componentkt.photopicker.picker.PickerManager
import com.lodz.android.componentkt.photopicker.picker.PickerUIConfig
import com.lodz.android.componentkt.photopicker.preview.PreviewManager
import com.lodz.android.corekt.anko.toArrayList

/**
 * 简单的九宫格实现
 * Created by zhouL on 2018/12/26.
 */
class SimpleNineGridView : NineGridView {

    /** 接口  */
    private var mListener: OnSimpleNineGridViewListener? = null
    /** 照片保存地址  */
    private var mCameraSavePath = ""
    /** 7.0的FileProvider名字  */
    private var mAuthority = ""
    /** 是否需要拍照  */
    private var isNeedCamera = true
    /** 是否需要预览Item  */
    private var isNeedItemPreview = true
    /** 是否需要缩放  */
    private var isScale = true
    /** 是否点击关闭预览  */
    private var isClickClosePreview = true
    /** 选择器UI配置  */
    private var mConfig = PickerUIConfig.createDefault()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setListener(object : OnNineGridViewListener {
            override fun onAddPic(addCount: Int) {
                PickerManager.create()
                        .setImgLoader(object : OnPhotoLoader<String> {
                            override fun displayImg(context: Context, source: String, imageView: ImageView) {
                                mListener?.onDisplayPickerImg(context, source, imageView)
                            }
                        })
                        .setPreviewImgLoader(object : OnPhotoLoader<String> {
                            override fun displayImg(context: Context, source: String, imageView: ImageView) {
                                mListener?.onDisplayPreviewImg(context, source, imageView)
                            }
                        })
                        .setOnPhotoPickerListener(object : OnPhotoPickerListener {
                            override fun onPickerSelected(photos: List<String>) {
                                addDatas(photos.toArrayList())
                            }
                        })
                        .setMaxCount(addCount)
                        .setScale(isScale)
                        .setNeedCamera(isNeedCamera)
                        .setNeedItemPreview(isNeedItemPreview)
                        .setClickClosePreview(isClickClosePreview)
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
                PreviewManager.create<String>()
                        .setScale(isScale)
                        .setPosition(position)
                        .setBackgroundColor(android.R.color.black)
                        .setStatusBarColor(android.R.color.black)
                        .setNavigationBarColor(android.R.color.black)
                        .setPagerTextColor(android.R.color.white)
                        .setPagerTextSize(14)
                        .setShowPagerText(true)
                        .setOnClickListener(object : com.lodz.android.componentkt.photopicker.contract.OnClickListener<String> {
                            override fun onClick(context: Context, source: String, position: Int, controller: PreviewController) {
                                if (isClickClosePreview) {
                                    controller.close()
                                }
                            }
                        })
                        .setImgLoader(object : OnPhotoLoader<String> {
                            override fun displayImg(context: Context, source: String, imageView: ImageView) {
                                mListener?.onDisplayPreviewImg(context, source, imageView)
                            }
                        })
                        .build(getPicData())
                        .open(getContext())
            }
        })
    }

    /** 配置数据，图片保存地址[savePath]，FileProvider名字[authority] */
    fun config(savePath: String, authority: String) {
        mCameraSavePath = savePath
        mAuthority = authority
    }

    /** 设置监听器[listener] */
    fun setOnSimpleNineGridViewListener(listener: OnSimpleNineGridViewListener) {
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

    /** 设置是否需要缩放[isScale] */
    fun setScale(isScale: Boolean) {
        this.isScale = isScale
    }

    /** 设置是否点击关闭预览[isClose] */
    fun setClickClosePreview(isClose: Boolean) {
        this.isClickClosePreview = isClose
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