package com.lodz.android.corekt.utils

/**
 * Android相机的YUV420（NV21）帮助类
 * @author zhouL
 * @date 2021/6/11
 */
object YUV420Utils {

    /** 将数据流[datas]翻转90度，原图宽度[width]，原图高度[height] */
    @JvmStatic
    fun rotateDegree90(data: ByteArray, imageWidth: Int, imageHeight: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
        // Rotate the Y luma
        var i = 0
        for (x in 0 until imageWidth) {
            for (y in imageHeight - 1 downTo 0) {
                yuv[i] = data[y * imageWidth + x]
                i++
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1
        var x = imageWidth - 1
        while (x > 0) {
            for (y in 0 until imageHeight / 2) {
                yuv[i] = data[imageWidth * imageHeight + y * imageWidth + x]
                i--
                yuv[i] = data[imageWidth * imageHeight + y * imageWidth + (x - 1)]
                i--
            }
            x -= 2
        }
        return yuv
    }

    /** 将数据流[datas]翻转180度，原图宽度[width]，原图高度[height] */
    @JvmStatic
    fun rotateDegree180(data: ByteArray, imageWidth: Int, imageHeight: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
        var count = 0
        var i = imageWidth * imageHeight - 1
        while (i >= 0) {
            yuv[count] = data[i]
            count++
            i--
        }
        i = imageWidth * imageHeight * 3 / 2 - 1
        while (i >= imageWidth * imageHeight) {
            yuv[count++] = data[i - 1]
            yuv[count++] = data[i]
            i -= 2
        }
        return yuv
    }

    /** 将数据流[datas]翻转270度，原图宽度[width]，原图高度[height] */
    @JvmStatic
    fun rotateDegree270(data: ByteArray, imageWidth: Int, imageHeight: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
        // Rotate the Y luma
        var i = 0
        for (x in imageWidth - 1 downTo 0) {
            for (y in 0 until imageHeight) {
                yuv[i] = data[y * imageWidth + x]
                i++
            }
        } // Rotate the U and V color components  	i = imageWidth*imageHeight;
        var x = imageWidth - 1
        while (x > 0) {
            for (y in 0 until imageHeight / 2) {
                yuv[i] = data[imageWidth * imageHeight + y * imageWidth + (x - 1)]
                i++
                yuv[i] = data[imageWidth * imageHeight + y * imageWidth + x]
                i++
            }
            x -= 2
        }
        return yuv
    }
}