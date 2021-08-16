package com.lodz.android.agiledevkt.modules.camera

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.event.TakePhotoEvent
import com.lodz.android.agiledevkt.databinding.ActivityCameraTakeBinding
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.camera.CameraHelper
import com.lodz.android.pandora.widget.camera.OnCameraListener
import io.reactivex.rxjava3.core.Observable
import okio.buffer
import okio.sink
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * 拍照页面
 * @author zhouL
 * @date 2021/1/21
 */
class CameraTakeActivity : AbsActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, CameraTakeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCameraTakeBinding by bindingLayout(ActivityCameraTakeBinding::inflate)

    /** 相机帮助类 */
    private var mCameraHelper: CameraHelper? = null

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mCameraHelper = CameraHelper(this, mBinding.surfaceView)
    }

    override fun setListeners() {
        super.setListeners()
        // 闪光灯
        mBinding.flashBtn.setOnClickListener {
            val isSuccess = mCameraHelper?.controlFlash() ?: false
            if (isSuccess) {
                mBinding.flashBtn.isSelected = !mBinding.flashBtn.isSelected
            } else {
                toastShort(R.string.camera_take_unsupport_flash)
            }
        }

        // 摄像头选择
        mBinding.cameraSwitchBtn.setOnClickListener {
            mCameraHelper?.exchangeCamera()
        }

        // 拍照
        mBinding.takeBtn.setOnClickListener {
            mCameraHelper?.takePic()
        }

        mCameraHelper?.setOnCameraListener(object : OnCameraListener {
            override fun onStatusChange(status: Int, msg: String) {

            }

            override fun onPreviewFrame(data: ByteArray?, camera: Camera) {
            }

            override fun onTakePic(data: ByteArray?, camera: Camera) {
                savePic(data)
            }

            override fun onFaceDetect(faces: ArrayList<RectF>) {
            }
        })
    }

    private fun savePic(data: ByteArray?) {
        if (data == null){
            toastShort(R.string.camera_take_fail)
            return
        }
        Observable.just(data)
            .map {
                val path = FileManager.getCacheFolderPath().append("Photo_").append(DateUtils.getCurrentFormatString(DateUtils.TYPE_3)).append(".jpg")
                FileUtils.createNewFile(path)
                val file = File(path)
                val rawBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val resultBitmap = if (mCameraHelper?.getCameraFacing() == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    // 手机前置摄像头要镜像并旋转270度
                    BitmapUtils.mirrorBitmap(BitmapUtils.rotateBitmap(rawBitmap, 270f))
                }else{
                    // 手机后置摄像头要旋转90度
                    BitmapUtils.rotateBitmap(rawBitmap, 90f)
                }
                file.sink().buffer().write(BitmapUtils.bitmapToByte(resultBitmap)).close()
                AlbumUtils.notifyScanImageCompat(getContext(), path)
                return@map path
            }
            .compose(RxUtils.ioToMainObservable())
            .compose(bindDestroyEvent())
            .subscribe(ProgressObserver.action<String>(
                context = getContext(),
                msg = getString(R.string.camera_take_processing),
                cancelable = false,
                next = {
                    EventBus.getDefault().post(TakePhotoEvent(it))
                    finish()
                },
                error = { e, isNetwork ->
                    toastShort(getString(R.string.camera_take_fail).append(e.cause))
                }
            ))
    }

    override fun finish() {
        super.finish()
        mCameraHelper?.releaseCamera()
    }
}