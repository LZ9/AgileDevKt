package com.lodz.android.corekt.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import com.lodz.android.corekt.anko.getScreenHeight
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

/**
 * Bitmap帮助类
 * Created by zhouL on 2018/7/2.
 */
object BitmapUtils {

    /** 把[bitmap]转为Bsae64，质量[quality]（0-100，默认70），转码类型[flags]默认Base64.NO_WRAP */
    @JvmStatic
    @JvmOverloads
    fun bitmapToBase64(
        bitmap: Bitmap, @IntRange(from = 0, to = 100) quality: Int = 70,
        flags: Int = Base64.NO_WRAP
    ): String {
        var reviseQuality = quality
        if (quality < 0) {
            reviseQuality = 0
        }
        if (quality > 100) {
            reviseQuality = 100
        }
        ByteArrayOutputStream().use { baos: ByteArrayOutputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, reviseQuality, baos)
            return Base64.encodeToString(baos.toByteArray(), flags) ?: ""
        }
    }

    /** Bitmap转Byte数组 */
    @JvmStatic
    fun bitmapToByte(bitmap: Bitmap): ByteArray {
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        return os.toByteArray()
    }

    /** Base64转为Bitmap */
    @JvmStatic
    @JvmOverloads
    fun base64ToBitmap(base64Data: String, flags: Int = Base64.NO_WRAP): Bitmap? {
        val bytes = base64ToByte(base64Data, flags)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /** Base64转为Byte数组 */
    @JvmStatic
    @JvmOverloads
    fun base64ToByte(base64Data: String, flags: Int = Base64.NO_WRAP): ByteArray = Base64.decode(base64Data, flags)

    /** [drawable]转为Bitmap，宽度[widthPx]和高度[heightPx]默认取[drawable]的值 */
    @JvmStatic
    @JvmOverloads
    fun drawableToBitmap(drawable: Drawable, @IntRange(from = 1) widthPx: Int = drawable.intrinsicWidth, @IntRange(from = 1) heightPx: Int = drawable.intrinsicHeight): Bitmap? {
        if (widthPx < 1 || heightPx < 1) {
            return null
        }
        val bitmap = Bitmap.createBitmap(
            widthPx,
            heightPx,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, widthPx, heightPx)
        drawable.draw(canvas)
        return bitmap
    }

    /** 把[view]转为Bitmap */
    @JvmStatic
    fun viewToBitmap(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }

    /** 把图片路径[path]转为Bitmap并对宽[widthPx]高[heightPx]进行质量压缩 */
    @JvmStatic
    fun compressBitmap(path: String, widthPx: Int, heightPx: Int): Bitmap? {
        try {
            val opts = BitmapFactory.Options()
            opts.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, opts)//计算出图片的宽高
            opts.inSampleSize = setInSampleSize(opts, widthPx, heightPx)
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888
            opts.inJustDecodeBounds = false
            FileInputStream(path).use { fis: FileInputStream ->
                return BitmapFactory.decodeStream(fis, null, opts)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 把图片路径[uri]转为Bitmap并对宽[widthPx]高[heightPx]进行质量压缩 */
    @JvmStatic
    fun compressBitmap(context: Context, uri: Uri, widthPx: Int, heightPx: Int): Bitmap? {
        context.contentResolver.openFileDescriptor(uri, "r")?.use { fd ->
            val opts = BitmapFactory.Options()
            BitmapFactory.decodeFileDescriptor(fd.fileDescriptor, null, opts)
            opts.inJustDecodeBounds = true
            opts.inSampleSize = setInSampleSize(opts, widthPx, heightPx)
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888
            opts.inJustDecodeBounds = false
            FileInputStream(fd.fileDescriptor).use { fs ->
                return BitmapFactory.decodeStream(fs, null, opts)
            }
        }
        return null
    }

    /** 把图片路径[uri]转为Bitmap */
    @JvmStatic
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        context.contentResolver.openFileDescriptor(uri, "r")?.use {
            return BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
        return null
    }


    /** 设置图片的缩放比例 */
    private fun setInSampleSize(opts: BitmapFactory.Options, widthPx: Int, heightPx: Int): Int {
        val pictureHeight = opts.outHeight//图片高度
        val pictureWidth = opts.outWidth//图片宽度
        if (pictureHeight <= heightPx && pictureWidth <= widthPx) {//图片宽高均小于屏幕宽高则不设置缩放比例
            return 1
        }
        return if (pictureHeight > pictureWidth) {//如果高大于宽
            Math.ceil(pictureHeight.toDouble() / heightPx.toDouble()).toInt()
        } else {
            Math.ceil(pictureWidth.toDouble() / widthPx.toDouble()).toInt()
        }
    }

    /** 左上 */
    const val LEFT_TOP = 1
    /** 左下 */
    const val LEFT_BOTTOM = 2
    /** 右上 */
    const val RIGHT_TOP = 3
    /** 右下 */
    const val RIGHT_BOTTOM = 4
    /** 中间 */
    const val CENTER = 5

    @IntDef(LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, CENTER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class CombinekLocationType

    /** 合并前景图[fg]和背景图[bg]，水印位置[location]，间距[marginPx] */
    @JvmStatic
    @JvmOverloads
    fun combineBitmap(fg: Bitmap, bg: Bitmap, @CombinekLocationType location: Int, marginPx: Int = 0): Bitmap {
        var newMargin = 0f
        if (marginPx > 0) {
            newMargin = marginPx.toFloat()
        }

        val bgWidth: Int// 背景宽
        val bgHeight: Int// 背景高

        val fgWidth: Int// 前景宽
        val fgHeight: Int// 前景高

        if (bg.width > fg.width) {
            bgWidth = bg.width
            fgWidth = fg.width
        } else {
            bgWidth = fg.width
            fgWidth = bg.width
        }

        if (bg.height > fg.height) {
            bgHeight = bg.height
            fgHeight = fg.height
        } else {
            bgHeight = fg.height
            fgHeight = bg.height
        }

        val bitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bg, 0f, 0f, null)

        when (location) {
            LEFT_TOP -> canvas.drawBitmap(fg, newMargin, newMargin, paint)
            LEFT_BOTTOM -> canvas.drawBitmap(fg, newMargin, bgHeight - fgHeight - newMargin, paint)
            RIGHT_TOP -> canvas.drawBitmap(fg, bgWidth - fgWidth - newMargin, newMargin, paint)
            RIGHT_BOTTOM -> canvas.drawBitmap(fg, bgWidth - fgWidth - newMargin, bgHeight - fgHeight - newMargin, paint)
            CENTER -> canvas.drawBitmap(
                fg,
                (bgWidth / 2.0 - fgWidth / 2.0).toFloat(),
                (bgHeight / 2.0 - fgHeight / 2.0).toFloat(),
                paint
            )
            else -> throw IllegalArgumentException("please use location in @CombinekLocationType")
        }
        return bitmap
    }

    @IntDef(LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class WatermarkLocationType


    /** 将水印图片[watermark]放置在原图片[src]上，水印位置[location]，间距[marginPx] */
    @JvmStatic
    @JvmOverloads
    fun createWatermarkBitmap(
        src: Bitmap,
        watermark: Bitmap, @WatermarkLocationType location: Int, @IntRange(from = 0) marginPx: Int = 0
    ): Bitmap {
        var newMargin = 0f
        if (marginPx > 0) {
            newMargin = marginPx.toFloat()
        }

        val width = src.width
        val height = src.height
        val watermarkWidth = watermark.width
        val watermarkHeight = watermark.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)// 创建一个新的和SRC长度宽度一样的位图
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(src, 0f, 0f, null)// 在 0，0坐标开始画入src

        when (location) {
            LEFT_TOP -> canvas.drawBitmap(watermark, newMargin, newMargin, null)// 在src的左上角画入水印
            LEFT_BOTTOM -> canvas.drawBitmap(watermark, newMargin, height - watermarkHeight - newMargin, null)// 在src的左下角画入水印
            RIGHT_TOP -> canvas.drawBitmap(watermark, width - watermarkWidth - newMargin, newMargin, null)// 在src的右上角画入水印
            RIGHT_BOTTOM -> canvas.drawBitmap(watermark, width - watermarkWidth - newMargin, height - watermarkHeight - newMargin, null)// 在src的右下角画入水印
            else -> throw IllegalArgumentException("please use location in @WatermarkLocationType")
        }

        canvas.save()// 保存
        canvas.restore()
        return bitmap
    }


    /** 将原图片转为灰度图 */
    @JvmStatic
    fun createGreyBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width// 获取位图的宽
        val height = bitmap.height// 获取位图的高

        val pixels = IntArray(width * height)// 通过位图的大小创建像素点数组

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val alpha = 0xFF shl 24
        for (i in 0 until height) {
            for (j in 0 until width) {
                var grey = pixels[width * i + j]

                val red = grey and 0x00FF0000 shr 16
                val green = grey and 0x0000FF00 shr 8
                val blue = grey and 0x000000FF

                grey = (red * 0.3 + green * 0.59 + blue * 0.11).toInt()
                grey = alpha or (grey shl 16) or (grey shl 8) or grey
                pixels[width * i + j] = grey
            }
        }
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }

    /** 将图片[bitmap]转为圆角[roundPx]图 */
    @JvmStatic
    @JvmOverloads
    fun createRoundedCornerBitmap(bitmap: Bitmap, @FloatRange(from = 0.0) roundPx: Float = 12f): Bitmap {

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = Color.parseColor("#424242")
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    /** 将图片[bitmap]增加倒影 */
    @JvmStatic
    fun createReflectionBitmap(bitmap: Bitmap): Bitmap {
        val reflectionGap = 0
        val width = bitmap.width
        val height = bitmap.height

        val matrix = Matrix()
        matrix.preScale(1f, -1f)

        val reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false)

        val bitmapWithReflection = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmapWithReflection)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val deafalutPaint = Paint()
        canvas.drawRect(0f, height.toFloat(), width.toFloat(), (height + reflectionGap).toFloat(), deafalutPaint)

        canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)

        val paint = Paint()
        val shader = LinearGradient(
            0f, bitmap.height.toFloat(), 0f,
            (bitmapWithReflection.height + reflectionGap).toFloat(), 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP
        )
        paint.shader = shader
        // Set the Transfer mode to be porter duff and destination in
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(
            0f,
            height.toFloat(),
            width.toFloat(),
            (bitmapWithReflection.height + reflectionGap).toFloat(),
            paint
        )

        return bitmapWithReflection
    }


    /** 将图片[bitmap]转为居中圆形图 */
    @JvmStatic
    fun createRoundBitmap(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val roundPx: Float
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float
        val dstLeft: Float
        val dstTop: Float
        val dstRight: Float
        val dstBottom: Float

        if (width <= height) {
            roundPx = (width / 2).toFloat()
            left = 0f
            right = width.toFloat()
            val clip = ((height - width) / 2).toFloat()
            top = clip
            bottom = height - clip
            height = width
            dstLeft = 0f
            dstTop = 0f
            dstRight = width.toFloat()
            dstBottom = width.toFloat()
        } else {
            roundPx = (height / 2).toFloat()
            val clip = ((width - height) / 2).toFloat()
            left = clip
            right = width - clip
            top = 0f
            bottom = height.toFloat()
            width = height
            dstLeft = 0f
            dstTop = 0f
            dstRight = height.toFloat()
            dstBottom = height.toFloat()
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = Color.parseColor("#424242")
        val paint = Paint()
        val src = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        val dst = Rect(dstLeft.toInt(), dstTop.toInt(), dstRight.toInt(), dstBottom.toInt())
        val rectF = RectF(dst)

        paint.isAntiAlias = true

        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, src, dst, paint)
        return output
    }

    /** 把图片[bitmap]垂直翻转 */
    @JvmStatic
    fun reverseBitmapVertical(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(1f, -1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }

    /** 更改图片[bitmap]色系，图片的亮暗程度值[delta]越小图片会越亮，取值范围(1,23) */
    @JvmStatic
    fun setBitmapTone(bitmap: Bitmap, @IntRange(from = 1, to = 23) delta: Int): Bitmap {
        var newDelta = delta
        if (delta > 23) {
            newDelta = 23
        }
        if (delta < 1) {
            newDelta = 1
        }

        // 设置高斯矩阵
        val gauss = intArrayOf(1, 2, 1, 2, 4, 2, 1, 2, 1)
        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        val pixels = IntArray(width * height)

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val length = height - 1
        for (i in 1 until length) {
            val len = width - 1
            for (k in 1 until len) {
                var idx = 0
                for (m in -1..1) {
                    for (n in -1..1) {
                        pixColor = pixels[(i + m) * width + k + n]

                        newR += Color.red(pixColor) * gauss[idx]
                        newG += Color.green(pixColor) * gauss[idx]
                        newB += Color.blue(pixColor) * gauss[idx]
                        idx++
                    }
                }
                newR /= newDelta
                newG /= newDelta
                newB /= newDelta
                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))
                pixels[i * width + k] = Color.argb(255, newR, newG, newB)
                newR = 0
                newG = 0
                newB = 0
            }
        }
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }

    /** 设置图片[bitmap]饱和度[value] */
    @JvmStatic
    fun setBitmapSaturation(bitmap: Bitmap, @FloatRange(from = 0.0, to = 2.0) value: Float): Bitmap {
        var newValue = value
        if (value <= 0f) {
            newValue = 0f
        }
        if (value >= 2f) {
            newValue = 2f
        }

        // 创建一个颜色矩阵
        val saturationColorMatrix = ColorMatrix()
        // 设置饱和度值
        saturationColorMatrix.setSaturation(newValue)
        // 创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(saturationColorMatrix)
        // 创建一个新的图片并创建画布
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        // 将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    /** 设置图片[bitmap]的亮度值[value] */
    @JvmStatic
    fun setBitmapLuminance(bitmap: Bitmap, @FloatRange(from = 0.0, to = 2.0) value: Float): Bitmap {
        var newValue = value
        if (value <= 0f) {
            newValue = 0f
        }
        if (value >= 2f) {
            newValue = 2f
        }

        // 创建一个颜色矩阵
        val lumColorMatrix = ColorMatrix()
        // 设置亮度值
        lumColorMatrix.setScale(newValue, newValue, newValue, 1f)
        // 创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(lumColorMatrix)
        // 创建一个新的图片并创建画布
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        // 将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    /** 设置图片[bitmap]的色相值[value] */
    @JvmStatic
    fun setBitmapHue(bitmap: Bitmap, @FloatRange(from = 0.0, to = 2.0) value: Float): Bitmap {
        var newValue = value
        if (value <= 0f) {
            newValue = 0f
        }
        if (value >= 2f) {
            newValue = 2f
        }

        // 创建一个颜色矩阵
        val hueColorMatrix = ColorMatrix()
        // 控制让红色区在色轮上旋转的角度
        hueColorMatrix.setRotate(0, newValue)
        // 控制让绿红色区在色轮上旋转的角度
        hueColorMatrix.setRotate(1, newValue)
        // 控制让蓝色区在色轮上旋转的角度
        hueColorMatrix.setRotate(2, newValue)
        // 创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(hueColorMatrix)
        // 创建一个新的图片并创建画布
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        // 将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }


    /** 把图片[bitmap]设置为怀旧效果 */
    @JvmStatic
    fun createNostalgicBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in 0 until height) {
            for (k in 0 until width) {
                val pixColor = pixels[width * i + k]
                val pixR = Color.red(pixColor)
                val pixG = Color.green(pixColor)
                val pixB = Color.blue(pixColor)
                val newR = (0.393 * pixR + 0.769 * pixG + 0.189 * pixB).toInt()
                val newG = (0.349 * pixR + 0.686 * pixG + 0.168 * pixB).toInt()
                val newB = (0.272 * pixR + 0.534 * pixG + 0.131 * pixB).toInt()
                val newColor = Color.argb(
                    255,
                    if (newR > 255) 255 else newR,
                    if (newG > 255) 255 else newG,
                    if (newB > 255) 255 else newB
                )
                pixels[width * i + k] = newColor
            }
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /** 对图片[bitmap]进行柔化效果处理 */
    @JvmStatic
    fun createSoftenBitmap(bitmap: Bitmap): Bitmap = setBitmapTone(bitmap, 16)

    /** 对图片[bitmap]进行光照效果处理，光源位置为X轴[centerX]和Y轴[centerY]，光照强度[strength] */
    @JvmStatic
    @JvmOverloads
    fun createSunshineBitmap(
        bitmap: Bitmap, centerX: Int, centerY: Int, @FloatRange(
            from = 0.0,
            to = 100.0
        ) strength: Float = 50f
    ): Bitmap {
        var newStrength = strength + 100
        if (newStrength >= 200f) {
            newStrength = 200f
        }
        if (newStrength <= 100f) {
            newStrength = 100f
        }

        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        val radius = Math.min(centerX, centerY)

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val length = height - 1
        for (i in 1 until length) {
            val len = width - 1
            for (k in 1 until len) {
                val pos = i * width + k
                val pixColor = pixels[pos]
                val pixR = Color.red(pixColor)
                val pixG = Color.green(pixColor)
                val pixB = Color.blue(pixColor)

                var newR = pixR
                var newG = pixG
                var newB = pixB

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                val distance = Math.pow((centerY - i).toDouble(), 2.0) + Math.pow((centerX - k).toDouble(), 2.0)
                if (distance.toInt() < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    val result = (newStrength * (1.0 - Math.sqrt(distance) / radius)).toInt()
                    newR = pixR + result
                    newG = pixG + result
                    newB = pixB + result
                }

                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))

                pixels[pos] = Color.argb(255, newR, newG, newB)
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /** 将图片[bitmap]进行底片效果处理 */
    @JvmStatic
    fun createFilmBitmap(bitmap: Bitmap): Bitmap {
        // RGBA的最大值
        val maxValue = 255
        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val length = height - 1
        for (i in 1 until length) {
            val len = width - 1
            for (k in 1 until len) {
                val pos = i * width + k
                val pixColor = pixels[pos]

                var newR = maxValue - Color.red(pixColor)
                var newG = maxValue - Color.green(pixColor)
                var newB = maxValue - Color.blue(pixColor)

                newR = Math.min(maxValue, Math.max(0, newR))
                newG = Math.min(maxValue, Math.max(0, newG))
                newB = Math.min(maxValue, Math.max(0, newB))

                pixels[pos] = Color.argb(maxValue, newR, newG, newB)
            }
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /** 将图片[bitmap]进行锐化效果处理 */
    @JvmStatic
    fun createSharpenBitmap(bitmap: Bitmap): Bitmap {
        // 拉普拉斯矩阵
        val laplacian = intArrayOf(-1, -1, -1, -1, 9, -1, -1, -1, -1)

        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        var newR = 0
        var newG = 0
        var newB = 0

        val alpha = 0.3f

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val length = height - 1
        for (i in 1 until length) {
            val len = width - 1
            for (k in 1 until len) {
                var idx = 0
                for (m in -1..1) {
                    for (n in -1..1) {
                        val pixColor = pixels[(i + n) * width + k + m]

                        newR += (Color.red(pixColor).toFloat() * laplacian[idx].toFloat() * alpha).toInt()
                        newG += (Color.green(pixColor).toFloat() * laplacian[idx].toFloat() * alpha).toInt()
                        newB += (Color.blue(pixColor).toFloat() * laplacian[idx].toFloat() * alpha).toInt()
                        idx++
                    }
                }

                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))

                pixels[i * width + k] = Color.argb(255, newR, newG, newB)
                newR = 0
                newG = 0
                newB = 0
            }
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /** 对图片[bitmap]进行浮雕效果处理 */
    @JvmStatic
    fun createEmbossBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val length = height - 1
        for (i in 1 until length) {
            val len = width - 1
            for (k in 1 until len) {
                val pos = i * width + k
                var pixColor = pixels[pos]

                val pixR = Color.red(pixColor)
                val pixG = Color.green(pixColor)
                val pixB = Color.blue(pixColor)

                pixColor = pixels[pos + 1]
                var newR = Color.red(pixColor) - pixR + 127
                var newG = Color.green(pixColor) - pixG + 127
                var newB = Color.blue(pixColor) - pixB + 127

                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))

                pixels[pos] = Color.argb(255, newR, newG, newB)
            }
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /** 对长图[bitmap]进行拼装和优化处理 */
    @Deprecated("如果图片太长可能会出现无法显示的问题")
    @JvmStatic
    fun createLongLargeBitmap(context: Context, bitmap: Bitmap): Bitmap {
        val bitmaps = createLongLargeBitmaps(context, bitmap)

        val imgWidth = bitmap.width
        var imgHeight = 0
        for (bmp in bitmaps) {
            imgHeight += bmp.height
        }

        val bigbitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bigbitmap)

        val paint = Paint()
        var height = 0f

        //将之前的bitmap取出来拼接成一个bitmap
        for (bmp in bitmaps) {
            canvas.drawBitmap(bmp, 0f, height, paint)
            height += bmp.height
            bmp.recycle()
        }
        return bigbitmap
    }

    /** 将长图[bitmap]切分成多个Bitmap对象 */
    @JvmStatic
    fun createLongLargeBitmaps(context: Context, bitmap: Bitmap): List<Bitmap> {
        val maxHeight = context.getScreenHeight()// 图片最大长度
        val bitmapList = ArrayList<Bitmap>()
        try {
            ByteArrayOutputStream().use { baos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                ByteArrayInputStream(baos.toByteArray()).use { inputStream ->
                    //BitmapRegionDecoder newInstance(InputStream is, boolean isShareable)
                    //用于创建BitmapRegionDecoder，isBm表示输入流，只有jpeg和png图片才支持这种方式，
                    // isShareable如果为true，那BitmapRegionDecoder会对输入流保持一个表面的引用，
                    // 如果为false，那么它将会创建一个输入流的复制，并且一直使用它。即使为true，程序也有可能会创建一个输入流的深度复制。
                    // 如果图片是逐步解码的，那么为true会降低图片的解码速度。如果路径下的图片不是支持的格式，那就会抛出异常
                    val decoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        BitmapRegionDecoder.newInstance(inputStream)
                    } else {
                        BitmapRegionDecoder.newInstance(inputStream, true)
                    }
                    if (decoder == null) {
                        return bitmapList
                    }

                    val imgWidth = decoder.width
                    val imgHeight = decoder.height

                    val opts = BitmapFactory.Options()

                    //计算图片要被切分成几个整块，
                    // 如果sum=0 说明图片的长度不足3000px，不进行切分 直接添加
                    // 如果sum>0 先添加整图，再添加多余的部分，否则多余的部分不足3000时底部会有空白
                    val sum = imgHeight / maxHeight
                    val redundant = imgHeight % maxHeight
                    val rect = Rect()

                    if (sum == 0) { //图片的长度 < 3000直接加载
                        bitmapList.add(bitmap)
                    } else {
                        for (i in 0 until sum) {
                            //需要注意：mRect.set(left, top, right, bottom)的第四个参数，
                            //也就是图片的高不能大于这里的4096
                            rect.set(0, i * maxHeight, imgWidth, (i + 1) * maxHeight)
                            bitmapList.add(decoder.decodeRegion(rect, opts))
                        }

                        //将多余的不足3000的部分作为尾部拼接
                        if (redundant > 0) {
                            rect.set(0, sum * maxHeight, imgWidth, imgHeight)
                            bitmapList.add(decoder.decodeRegion(rect, opts))
                        }
                    }
                    return bitmapList
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmapList
    }

    /** 将图片[bitmap]旋转[angle]角度 */
    @JvmStatic
    fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /** 把图片[bitmap]水平翻转 */
    @JvmStatic
    fun reverseBitmapHorizontal(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1f, 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }

    /** Bitmap镜像翻转 */
    @JvmStatic
    fun mirrorBitmap(rawBitmap: Bitmap): Bitmap {
        var matrix = Matrix()
        matrix.postScale(-1f, 1f)
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
    }

    /** 把相机的预览图数据[data]，根绝给定宽[width]高[height]转Bitmap */
    @JvmStatic
    fun createPreviewFrameBitmap(data: ByteArray?, width: Int, height: Int): Bitmap? {
        if (data == null){
            return null
        }
        val yuvImage = YuvImage(data, ImageFormat.NV21, width, height, null)
        ByteArrayOutputStream().use {
            yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, it)
            val rawImage = it.toByteArray()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            return BitmapFactory.decodeByteArray(rawImage, 0, rawImage.size, options)
        }
    }

}