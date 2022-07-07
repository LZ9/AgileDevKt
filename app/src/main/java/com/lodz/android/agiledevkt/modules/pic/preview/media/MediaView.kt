package com.lodz.android.agiledevkt.modules.pic.preview.media

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnIO
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.media.*
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 多媒体展示控件
 * @author zhouL
 * @date 2022/6/20
 */
class MediaView : LinearLayout {

    companion object {
        private const val TYPE_UNKNOWN_FILE = 0
        private const val TYPE_IMAGE = 1
        private const val TYPE_VIDEO = 2
        private const val TYPE_AUDIO = 3
    }

    /** 图片 */
    private val mImgView by bindView<ImageView>(R.id.img_view)
    /** 可缩放图片 */
    private val mPhotoView by bindView<PhotoView>(R.id.photo_view)
    /** 视频 */
    private val mVideoView by bindView<VideoView>(R.id.video_view)
    /** 播放按钮 */
    private val mPlayBtn by bindView<ImageView>(R.id.play_btn)
    /** 底部栏 */
    private val mBottomLayout by bindView<LinearLayout>(R.id.bottom_layout)
    /** 进度条 */
    private val mDurationTv by bindView<TextView>(R.id.duration_tv)

    /** 文件数据 */
    private var mDocumentWrapper: DocumentWrapper? = null
    /** 文件类型 */
    private var mDocumentType = TYPE_UNKNOWN_FILE
    /** 图片是否缩放 */
    private val isScale: Boolean

    private var mDisposable: Disposable? = null

    constructor(context: Context?, isScale: Boolean) : super(context) {
        this.isScale = isScale
        init(null)
    }

    constructor(context: Context?, isScale: Boolean, attrs: AttributeSet?) : super(context, attrs) {
        this.isScale = isScale
        init(attrs)
    }

    constructor(context: Context?, isScale: Boolean, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.isScale = isScale
        init(attrs)
    }

    constructor(context: Context?, isScale: Boolean, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.isScale = isScale
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_media, this)
        setListeners()
    }

    fun setData(wrapper: DocumentWrapper) {
        mDocumentWrapper = wrapper
        if (wrapper.documentFile.type.isImageMimeType() || wrapper.documentFile.name.isImageSuffix()){
            mDocumentType = TYPE_IMAGE
        }
        if (wrapper.documentFile.type.isVideoMimeType() || wrapper.documentFile.name.isVideoSuffix()){
            mDocumentType = TYPE_VIDEO
            mVideoView.setVideoURI(wrapper.documentFile.uri)
        }
        if (wrapper.documentFile.type.isAudioMimeType() || wrapper.documentFile.name.isAudioSuffix()){
            mDocumentType = TYPE_AUDIO
        }
        updateUI(wrapper, mDocumentType)
    }

    private fun updateUI(wrapper: DocumentWrapper, documentType: Int) {
        mImgView.visibility = View.GONE
        mPhotoView.visibility = View.GONE
        mVideoView.visibility = View.GONE
        mPlayBtn.visibility = View.GONE
        mBottomLayout.visibility = View.GONE
        if (documentType == TYPE_IMAGE){
            mImgView.visibility = isScale.then { View.GONE } ?: View.VISIBLE
            mPhotoView.visibility = isScale.then { View.VISIBLE } ?: View.GONE
            val img = if (mImgView.visibility == View.VISIBLE) mImgView else mPhotoView
            ImageLoader.create(context)
                .loadUri(wrapper.documentFile.uri)
                .setPlaceholder(R.drawable.pandora_ic_img)
                .setError(R.drawable.pandora_ic_img)
                .setFitCenter()
                .into(img)
            return
        }
        if (documentType == TYPE_VIDEO){
            mImgView.visibility = View.VISIBLE
            mPlayBtn.visibility = View.VISIBLE
            mBottomLayout.visibility = View.VISIBLE
            ImageLoader.create(context)
                .loadUri(wrapper.documentFile.uri)
                .setPlaceholder(R.drawable.pandora_ic_video)
                .setError(R.drawable.pandora_ic_video)
                .setFitCenter()
                .into(mImgView)
            return
        }
        if (documentType == TYPE_AUDIO){
            mImgView.visibility = View.VISIBLE
            mPlayBtn.visibility = View.VISIBLE
            mBottomLayout.visibility = View.VISIBLE
            return
        }
        mImgView.visibility = View.VISIBLE
        mImgView.setImageResource(R.drawable.pandora_ic_file)
    }

    private fun setListeners() {
        mPlayBtn.setOnClickListener {
            mVideoView.visibility = View.VISIBLE
            playVideo()
            mImgView.visibility = View.GONE
            mPlayBtn.visibility = View.GONE
        }
        this@MediaView.setOnClickListener {
            if (mVideoView.visibility == View.VISIBLE){
                mPlayBtn.visibility = View.VISIBLE
                pauseVideo()
            }
        }
        mVideoView.setOnPreparedListener {
            PrintLog.d("testtag", "setOnPreparedListener -> ${mVideoView.duration}")
            mDurationTv.text = "0 / ${mVideoView.duration}"
        }

        mVideoView.setOnErrorListener { mp, what, extra ->
            toastShort("播放异常")
            return@setOnErrorListener false
        }

        mVideoView.setOnCompletionListener {
            disposeInterval()
            PrintLog.d("testtag", "setOnCompletionListener -> ${mVideoView.duration}")
            mDurationTv.text = "${mVideoView.duration} / ${mVideoView.duration}"
            mPlayBtn.visibility = View.VISIBLE
        }
    }

    private fun pauseVideo() {
        if (mVideoView.isPlaying) {
            mVideoView.pause()
            disposeInterval()
        }
    }

    private fun playVideo() {
        if (!mVideoView.isPlaying) {
            mVideoView.start()
            intervalDuration()
        }
    }

    private fun intervalDuration(){
        disposeInterval()
        mDisposable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
            .compose(RxUtils.ioToMainObservable())
            .subscribe {
                PrintLog.e("testtag", "interval -> ${mVideoView.currentPosition}")
                mDurationTv.setText("${mVideoView.currentPosition} / ${mVideoView.duration}")
            }
    }

    private fun disposeInterval(){
        if (mDisposable != null) {
            mDisposable?.dispose()
            mDisposable = null
        }
    }

    fun getImageView(): ImageView = isScale.then { mPhotoView } ?: mImgView

    fun detached() {
        if (mPhotoView.visibility == View.VISIBLE) {
            mPhotoView.attacher.update()
        }
        disposeInterval()
        if (mVideoView.isPlaying) {
            mVideoView.pause()
            mVideoView.visibility = View.GONE
            mImgView.visibility = View.VISIBLE
            mPlayBtn.visibility = View.VISIBLE
            runOnIO {
                mVideoView.stopPlayback()
            }
        }

    }

}