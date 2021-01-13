package com.lodz.android.agiledevkt.modules.camera

import android.Manifest
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import kotlinx.android.synthetic.main.activity_camera.*
import permissions.dispatcher.*

/**
 * 相机测试类
 * @author zhouL
 * @date 2021/1/13
 */
@RuntimePermissions
class CameraActivity : AppCompatActivity() {
    companion object {
        const val TYPE_TAG = "type"
        const val TYPE_CAPTURE = 0
        const val TYPE_RECORD = 1
    }

    var lock = false //控制MediaRecorderHelper的初始化

    private lateinit var mCameraHelper: CameraHelper
    private var mMediaRecorderHelper: MediaRecorderHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermissionWithPermissionCheck()//申请权限
        } else {
            initData()
        }
    }

    private fun savePic(data: ByteArray?) {
        toastShort("图片已保存")
//        thread {
//            try {
//                val temp = System.currentTimeMillis()
//                val picFile = FileUtil.createCameraFile()
//                if (picFile != null && data != null) {
//                    val rawBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
//                    val resultBitmap = if (mCameraHelper.mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT)
//                        BitmapUtils.mirror(BitmapUtils.rotate(rawBitmap, 270f))
//                    else
//                        BitmapUtils.rotate(rawBitmap, 90f)
//
//                    picFile.sink().buffer().write(BitmapUtils.toByteArray(resultBitmap)).close()
//                    runOnUiThread {
//                        toast("图片已保存! ${picFile.absolutePath}")
//                        log("图片已保存! 耗时：${System.currentTimeMillis() - temp}    路径：  ${picFile.absolutePath}")
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                runOnUiThread {
//                    toast("保存图片失败！")
//                }
//            }
//        }
    }

    override fun onDestroy() {
        mCameraHelper.releaseCamera()
        mMediaRecorderHelper?.let {
            if (it.isRunning)
                it.stopRecord()
            it.release()
        }
        super.onDestroy()
    }

    private fun initData() {

        mCameraHelper = CameraHelper(this, surfaceView)
        mCameraHelper.addCallBack(object : CameraHelper.CallBack {
            override fun onFaceDetect(faces: ArrayList<RectF>) {
//                faceView.setFaces(faces)
            }

            override fun onTakePic(data: ByteArray?) {
                savePic(data)
                btnTakePic.isClickable = true
            }

            override fun onPreviewFrame(data: ByteArray?) {
                if (!lock) {
                    mCameraHelper.getCamera()?.let {
                        mMediaRecorderHelper = MediaRecorderHelper(this@CameraActivity, mCameraHelper.getCamera()!!, mCameraHelper.mDisplayOrientation, mCameraHelper.mSurfaceHolder.surface)
                    }
                    lock = true
                }
            }
        })

        if (intent.getIntExtra(TYPE_TAG, TYPE_RECORD) == TYPE_RECORD) { //录视频
            btnTakePic.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }

        btnTakePic.setOnClickListener { mCameraHelper.takePic() }
        ivExchange.setOnClickListener { mCameraHelper.exchangeCamera() }
        btnStart.setOnClickListener {
            ivExchange.isClickable = false
            btnStart.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
            mMediaRecorderHelper?.startRecord()
        }
        btnStop.setOnClickListener {
            btnStart.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
            ivExchange.isClickable = true
            mMediaRecorderHelper?.stopRecord()
        }
    }

    /** 权限申请成功 */
    @NeedsPermission(
            Manifest.permission.CAMERA,// 相机
            Manifest.permission.RECORD_AUDIO//录音
    )
    fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            return
        }
        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            return
        }
        initData()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(
            Manifest.permission.CAMERA,// 相机
            Manifest.permission.RECORD_AUDIO//录音
    )
    fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    @OnPermissionDenied(
            Manifest.permission.CAMERA,// 相机
            Manifest.permission.RECORD_AUDIO//录音
    )
    fun onDenied() {
        onRequestPermissionWithPermissionCheck()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(
            Manifest.permission.CAMERA,// 相机
            Manifest.permission.RECORD_AUDIO//录音
    )
    fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        goAppDetailSetting()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}