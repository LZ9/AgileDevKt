package com.lodz.android.corekt.anko

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

/**
 * 相机扩展类
 * @author zhouL
 * @date 2026/8/27
 */

/** 获取相机列表 */
fun Context.getCameraIdList(): Array<String> {
    val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    return try {
        cameraManager.cameraIdList
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
}

/** 是否存在[direction]方向的摄像头，默认判断是否存在后置 */
fun Context.hasCamera(direction: Int = CameraCharacteristics.LENS_FACING_BACK): Boolean {
    var hasBackCamera = false
    val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraIdList = try {
        cameraManager.cameraIdList
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
    cameraIdList.forEach { cameraId ->
        try {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                direction -> hasBackCamera = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return hasBackCamera
}

/** 获取[direction]方向摄像头的最大分辨率，默认获取后置摄像头 */
fun Context.getCameraMaxResolutions(direction: Int = CameraCharacteristics.LENS_FACING_BACK): Int {
    var maxResolutions = 0
    val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraIdList = try {
        cameraManager.cameraIdList
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
    cameraIdList.forEach { cameraId ->
        try {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                direction -> {
                    val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    val sizes = map?.getOutputSizes(ImageFormat.JPEG)?.toList()?: emptyList()
                    sizes.forEach {
                        val resolutions = it.width * it.height
                        if (resolutions > maxResolutions){
                            maxResolutions = resolutions
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return maxResolutions
}