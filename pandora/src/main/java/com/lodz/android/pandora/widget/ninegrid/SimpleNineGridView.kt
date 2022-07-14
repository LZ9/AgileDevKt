package com.lodz.android.pandora.widget.ninegrid

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.lodz.android.corekt.anko.toArrayList
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.pandora.picker.file.PickerManager
import com.lodz.android.pandora.picker.file.PickerUIConfig
import com.lodz.android.pandora.picker.preview.PreviewManager
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder


/**
 * 简单的九宫格实现
 * Created by zhouL on 2018/12/26.
 */
class SimpleNineGridView : NineGridView<DocumentWrapper> {

    /** 接口 */
    private var mPdrListener: OnSimpleNineGridViewListener<DocumentWrapper>? = null
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
        setListener(object :OnNineGridViewListener<DocumentWrapper>{
            override fun onAddPic(addCount: Int) {
                val listener = mPdrListener ?: return
                PickerManager.pickPhoneAlbum()
                    .setImgLoader { context, source, imageView ->
                        mPdrListener?.onDisplayPickerImg(context, source, imageView)
                    }
                    .setPreviewView(listener.createImageView())
                    .setOnFilePickerListener{
                        addDatas(it.toArrayList())
                    }
                    .setMaxCount(addCount)
                    .setNeedCamera(isPdrNeedCamera)
                    .setNeedPreview(isPdrNeedItemPreview)
                    .setPickerUIConfig(mPdrConfig)
                    .setAuthority(mPdrAuthority)
                    .open(context)
            }

            override fun onDisplayImg(context: Context, data: DocumentWrapper, imageView: ImageView) {
                mPdrListener?.onDisplayNineGridImg(context, data, imageView)
            }

            override fun onDeletePic(data: DocumentWrapper, position: Int) {
                removeDatas(position)
            }

            override fun onClickPic(data: DocumentWrapper, position: Int) {
                val listener = mPdrListener ?: return
                PreviewManager.create<DocumentWrapper, DataViewHolder>()
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
        mPdrCameraSavePath = savePath
        mPdrAuthority = authority
    }

    /** 设置监听器[listener] */
    fun setOnSimpleNineGridViewListener(listener: OnSimpleNineGridViewListener<DocumentWrapper>) {
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

    override fun addData(data: ArrayList<DocumentWrapper>) {}

    override fun removeData(position: Int) {}

    override fun setOnNineGridViewListener(listener: OnNineGridViewListener<DocumentWrapper>) {}

    private fun addDatas(data: ArrayList<DocumentWrapper>) {
        super.addData(data)
    }

    private fun removeDatas(position: Int) {
        super.removeData(position)
    }

    private fun setListener(listener: OnNineGridViewListener<DocumentWrapper>) {
        super.setOnNineGridViewListener(listener)
    }
}