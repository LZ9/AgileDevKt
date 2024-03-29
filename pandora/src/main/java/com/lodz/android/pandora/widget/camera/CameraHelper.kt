package com.lodz.android.pandora.widget.camera

import android.app.Activity
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.RectF
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

/**
 * 相机帮助类
 * @author zhouL
 * @date 2021/1/13
 */
open class CameraHelper @JvmOverloads constructor(activity: Activity, surfaceView: SurfaceView, cameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_BACK) {

    companion object {
        const val LOG_TAG = "CameraHelper"
    }

    /** Camera对象 */
    protected var mCamera: Camera? = null
    /** Camera对象的参数 */
    protected lateinit var mParameters: Camera.Parameters

    /** 用于预览的SurfaceView对象 */
    protected var mSurfaceView = surfaceView
    /** SurfaceHolder对象 */
    protected var mSurfaceHolder = mSurfaceView.holder

    protected var mActivity = activity
    /** 监听器 */
    protected var mListener: OnCameraListener? = null

    /** 摄像头方向（默认背面） */
    protected var mCameraFacing = cameraFacing
    /** 预览旋转的角度 */
    protected var mDisplayOrientation: Int = 0

    /** 保存图片的宽 */
    protected var picWidth = 2160
    /** 保存图片的高 */
    protected var picHeight = 3840

    init {
        mSurfaceHolder.addCallback(object :SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (mCamera == null) {
                    openCamera(mCameraFacing)
                }
                startPreview()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                releaseCamera()
            }
        })
    }

    /** 拍照 */
    fun takePic() {
        mCamera?.takePicture({}, null, { data, camera ->
            camera.startPreview()
            mListener?.onTakePic(data, camera)
        })
    }

    /** 打开相机，[cameraFacing]摄像头位置 */
    private fun openCamera(cameraFacing: Int): Boolean {
        val isSupport = isSupportCameraFacing(cameraFacing)
        if (isSupport) {
            try {
                mCamera = Camera.open(cameraFacing)
                initParameters()
                mCamera?.setPreviewCallback { data, camera ->
                    mListener?.onPreviewFrame(data, camera)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mListener?.onStatusChange(OnCameraListener.CAMERA_UNSUPPORT_CAMERA_FACING, "不支持该摄像头")
                return false
            }
        }
        mListener?.onStatusChange(OnCameraListener.CAMERA_UNSUPPORT_CAMERA_FACING, "不支持该摄像头")
        return false
    }

    /** 配置相机参数 */
    private fun initParameters() {
        try {
            val camera = mCamera
            if (camera == null){
                mListener?.onStatusChange(OnCameraListener.CAMERA_OPEN_FAIL, "相机打开失败")
                return
            }
            mParameters = camera.parameters
            mParameters.previewFormat = ImageFormat.NV21

            //获取与指定宽高相等或最接近的尺寸
            //设置预览尺寸
            val bestPreviewSize = getBestSize(mSurfaceView.width, mSurfaceView.height, mParameters.supportedPreviewSizes)
            bestPreviewSize?.let {
                mParameters.setPreviewSize(it.width, it.height)
            }
            //设置保存图片尺寸
            val bestPicSize = getBestSize(picWidth, picHeight, mParameters.supportedPictureSizes)
            bestPicSize?.let {
                mParameters.setPictureSize(it.width, it.height)
            }
            //对焦模式
            if (isSupportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
                mParameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            }
            mParameters = configParameters(mParameters)
            camera.parameters = mParameters
            mListener?.onStatusChange(OnCameraListener.CAMERA_PARAMETERS_INIT_SUCCESS, "相机参数初始化成功")
            mListener?.onStatusChange(OnCameraListener.CAMERA_OPEN_SUCCESS, "相机打开成功")
        } catch (e: Exception) {
            e.printStackTrace()
            toast("相机初始化失败!")
            mListener?.onStatusChange(OnCameraListener.CAMERA_PARAMETERS_INIT_FAIL, "相机参数初始化失败")
        }
    }

    protected open fun configParameters(parameters: Camera.Parameters): Camera.Parameters {
        return parameters
    }

    /** 开始预览 */
    fun startPreview() {
        mCamera?.setPreviewDisplay(mSurfaceHolder)
        mCamera?.setDisplayOrientation(getCameraDisplayOrientation(mActivity))
        mCamera?.startPreview()
        startFaceDetect()
    }

    private fun startFaceDetect() {
        mCamera?.startFaceDetection()
        mCamera?.setFaceDetectionListener { faces, _ ->
            mListener?.onFaceDetect(transForm(faces))
            Log.d(LOG_TAG,"检测到 ${faces.size} 张人脸")
        }
    }

    /** 判断是否支持某一对焦模式 */
    private fun isSupportFocus(focusMode: String): Boolean {
        var autoFocus = false
        val listFocusMode = mParameters.supportedFocusModes
        for (mode in listFocusMode) {
            Log.d(LOG_TAG,"相机支持的对焦模式： $mode")
            if (mode == focusMode){
                autoFocus = true
            }
        }
        return autoFocus
    }

    /** 切换摄像头 */
    fun exchangeCamera() {
        releaseCamera()
        mCameraFacing = if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK)
            Camera.CameraInfo.CAMERA_FACING_FRONT
        else
            Camera.CameraInfo.CAMERA_FACING_BACK

        openCamera(mCameraFacing)
        startPreview()
    }

    /** 释放相机 */
    fun releaseCamera() {
        mCamera?.stopPreview()
        mCamera?.setPreviewCallback(null)
        mCamera?.release()
        mCamera = null
    }

    /** 获取与指定宽高相等或最接近的尺寸 */
    private fun getBestSize(width: Int, height: Int, sizeList: List<Camera.Size>): Camera.Size? {
        val targetRatio = height.toDouble() / width //传入的高宽比
        Log.v(LOG_TAG,"目标尺寸 ：$height * $width ，   高宽比  $targetRatio")
        val suitableList = ArrayList<Camera.Size>()
        for (size in sizeList) {
            val sizeRatio = size.width.toDouble() / size.height
            Log.i(LOG_TAG,"系统支持的尺寸 : ${size.width} * ${size.height} ,    比例$sizeRatio")
            if (targetRatio <= sizeRatio){
                suitableList.add(size)
            }
        }
        if (suitableList.size == 0){
            return null
        }
        if (suitableList.size == 1){
            Log.d(LOG_TAG,"最优尺寸 ：${suitableList[0].height} * ${suitableList[0].width}")
            return suitableList[0]
        }
        var bestSize: Camera.Size = suitableList[0]
        suitableList.forEach {
            if (it.width > bestSize.width ){
                bestSize = it
            }
        }
        Log.d(LOG_TAG,"最优尺寸 ：${bestSize.width} * ${bestSize.height}")
        return bestSize
    }

    /** 获取预览旋转的角度 */
    private fun getCameraDisplayOrientation(activity: Activity) :Int{
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(mCameraFacing, info)
        val rotation = activity.windowManager.defaultDisplay.rotation

        var screenDegree = 0
        when (rotation) {
            Surface.ROTATION_0 -> screenDegree = 0
            Surface.ROTATION_90 -> screenDegree = 90
            Surface.ROTATION_180 -> screenDegree = 180
            Surface.ROTATION_270 -> screenDegree = 270
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mDisplayOrientation = (info.orientation + screenDegree) % 360
            mDisplayOrientation = (360 - mDisplayOrientation) % 360          // compensate the mirror
        } else {
            mDisplayOrientation = (info.orientation - screenDegree + 360) % 360
        }
        Log.d(LOG_TAG,"屏幕的旋转角度 : $rotation")
        Log.d(LOG_TAG,"setDisplayOrientation(result) : $mDisplayOrientation")
        return mDisplayOrientation
    }

    /** 判断是否支持[cameraFacing]摄像头位置 */
    private fun isSupportCameraFacing(cameraFacing: Int): Boolean {
        val info = Camera.CameraInfo()
        for (i in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(i, info)
            if (info.facing == cameraFacing) {
                return true
            }
        }
        return false
    }

    /** 将相机中用于表示人脸矩形的坐标转换成UI页面的坐标 */
    private fun transForm(faces: Array<Camera.Face>): ArrayList<RectF> {
        val matrix = Matrix()
        // Need mirror for front camera.
        val mirror = (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        matrix.setScale(if (mirror) -1f else 1f, 1f)
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(mDisplayOrientation.toFloat())
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(mSurfaceView.width / 2000f, mSurfaceView.height / 2000f)
        matrix.postTranslate(mSurfaceView.width / 2f, mSurfaceView.height / 2f)

        val rectList = ArrayList<RectF>()
        for (face in faces) {
            val srcRect = RectF(face.rect)
            val dstRect = RectF(0f, 0f, 0f, 0f)
            matrix.mapRect(dstRect, srcRect)
            rectList.add(dstRect)
        }
        return rectList
    }

    /** 控制相机闪光灯 */
    fun controlFlash(): Boolean {
        val flashMode = mParameters.flashMode
        if (flashMode.isNullOrEmpty()){
            return false
        }
        if (flashMode == Camera.Parameters.FLASH_MODE_TORCH) {
            mParameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
        } else {
            mParameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            mParameters.exposureCompensation = -1
        }
        try {
            if (mCamera == null){
                return false
            }
            mCamera?.parameters = mParameters
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
    }

    fun getCamera(): Camera? = mCamera

    /** 获取摄像头方向 */
    fun getCameraFacing(): Int = mCameraFacing

    /** 获取预览旋转的角度 */
    fun getDisplayOrientation(): Int = mDisplayOrientation

    fun setOnCameraListener(listener: OnCameraListener) {
        this.mListener = listener
    }

}