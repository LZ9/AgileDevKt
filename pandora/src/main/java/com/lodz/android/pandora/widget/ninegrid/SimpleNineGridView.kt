package com.lodz.android.pandora.widget.ninegrid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.pandora.picker.file.PickerUIConfig


/**
 * 简单的九宫格实现
 * Created by zhouL on 2018/12/26.
 */
class SimpleNineGridView<V : View> : NineGridView {

    /** 接口 */
    private var mPdrListener: OnSimpleNineGridViewListener<V>? = null
    /** 照片保存地址 */
    private var mPdrCameraSavePath = ""
    /** 7.0的FileProvider名字 */
    private var mPdrAuthority = ""
    /** 是否需要拍照 */
    private var isPdrNeedCamera = true
    /** 是否需要预览Item */
    private var isPdrNeedItemPreview = true
    /** 选择器UI配置 */
    private var mPdrConfig = PickerUIConfig.createDefault()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setListener(object : OnNineGridViewListener {
            override fun onAddPic(addCount: Int) {
                val listener = mPdrListener ?: return
//                PickerManager.create<V>()
//                        .setImgLoader { context, source, imageView ->
//                            mPdrListener?.onDisplayPickerImg(context, source, imageView)
//                        }
//                        .setImageView(listener.createImageView())
//                        .setOnPhotoPickerListener { photos ->
//                            addDatas(photos.toArrayList())
//                        }
//                        .setMaxCount(addCount)
//                        .setNeedCamera(isPdrNeedCamera)
//                        .setNeedItemPreview(isPdrNeedItemPreview)
//                        .setPickerUIConfig(mPdrConfig)
//                        .setCameraSavePath(mPdrCameraSavePath)
//                        .setAuthority(mPdrAuthority)
//                        .build()
//                        .open(context)
            }

            override fun onDisplayImg(context: Context, data: PicInfo, imageView: ImageView) {
                mPdrListener?.onDisplayNineGridImg(context, data, imageView)
            }

            override fun onDeletePic(data: PicInfo, position: Int) {
                removeDatas(position)
            }

            override fun onClickPic(data: PicInfo, position: Int) {
                val listener = mPdrListener ?: return
//                PreviewManager.create<V, PicInfo>()
//                        .setPosition(position)
//                        .setBackgroundColor(android.R.color.black)
//                        .setStatusBarColor(android.R.color.black)
//                        .setNavigationBarColor(android.R.color.black)
//                        .setPagerTextColor(android.R.color.white)
//                        .setPagerTextSize(14)
//                        .setShowPagerText(true)
//                        .setImageView(listener.createImageView())
//                        .build(getPicData())
//                        .open(context)
            }
        })
    }

    /** 配置数据，图片保存地址[savePath]，FileProvider名字[authority] */
    fun config(savePath: String, authority: String) {
        mPdrCameraSavePath = savePath
        mPdrAuthority = authority
    }

    /** 设置监听器[listener] */
    fun setOnSimpleNineGridViewListener(listener: OnSimpleNineGridViewListener<V>) {
        mPdrListener = listener
    }

    /** 设置是否需要拍照[isNeed] */
    fun setNeedCamera(isNeed: Boolean) {
        isPdrNeedCamera = isNeed
    }

    /** 设置是否需要预览Item[isNeed] */
    fun setNeedItemPreview(isNeed: Boolean) {
        isPdrNeedItemPreview = isNeed
    }

    /** 设置选择器UI配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig) {
        this.mPdrConfig = config
    }

    override fun addData(data: ArrayList<PicInfo>) {}

    override fun removeData(position: Int) {}

    override fun setOnNineGridViewListener(listener: OnNineGridViewListener) {}

    private fun addDatas(data: ArrayList<PicInfo>) {
        super.addData(data)
    }

    private fun removeDatas(position: Int) {
        super.removeData(position)
    }

    private fun setListener(listener: OnNineGridViewListener) {
        super.setOnNineGridViewListener(listener)
    }
}