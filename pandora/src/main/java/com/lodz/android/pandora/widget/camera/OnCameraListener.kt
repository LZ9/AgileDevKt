package com.lodz.android.pandora.widget.camera

import android.graphics.RectF

/**
 * 相机监听器
 * @author zhouL
 * @date 2021/1/21
 */
interface OnCameraListener {

    companion object{
        /** 相机打开成功 */
        const val CAMERA_OPEN_SUCCESS = 1
        /** 相机打开失败 */
        const val CAMERA_OPEN_FAIL = 2
        /** 不支持的摄像头类型 */
        const val CAMERA_UNSUPPORT_CAMERA_FACING = 3
    }

    /** 状态变化 */
    fun onStatusChange(status: Int, msg: String)

    /** 预览 */
    fun onPreviewFrame(data: ByteArray?)

    /** 拍照 */
    fun onTakePic(data: ByteArray?)

    /** 人脸框 */
    fun onFaceDetect(faces: ArrayList<RectF>)
}