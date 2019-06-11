package com.lodz.android.imageloaderkt.glide.impl

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.RequestManager
import com.lodz.android.imageloaderkt.ImageloaderManager
import com.lodz.android.imageloaderkt.contract.ImageLoaderContract
import com.lodz.android.imageloaderkt.contract.ResourceContract
import com.lodz.android.imageloaderkt.glide.config.GlideApp
import java.io.File

/**
 * Glide图片加载库
 * @author zhouL
 * @date 2019/6/11
 */
internal class GlideResource private constructor() : ResourceContract {

    /** 构造对象实体 */
    private lateinit var mGlideBuilderBean: GlideBuilderBean
    /** 请求管理对象 */
    private lateinit var mRequestManager: RequestManager

    companion object {
        /** 创建Glide加载库 */
        @JvmStatic
        fun with(context: Context): GlideResource {
            val imageLoader = GlideResource()
            imageLoader.mGlideBuilderBean = getGlideBuilderBean(ImageloaderManager.get().getBuilder())
            imageLoader.mRequestManager = GlideApp.with(context)
            return imageLoader
        }

        /** 创建Glide加载库 */
        @JvmStatic
        fun with(activity: Activity): GlideResource {
            val imageLoader = GlideResource()
            imageLoader.mGlideBuilderBean = getGlideBuilderBean(ImageloaderManager.get().getBuilder())
            imageLoader.mRequestManager = GlideApp.with(activity)
            return imageLoader
        }

        /** 创建Glide加载库 */
        @JvmStatic
        fun with(fragmentActivity: FragmentActivity): GlideResource {
            val imageLoader = GlideResource()
            imageLoader.mGlideBuilderBean = getGlideBuilderBean(ImageloaderManager.get().getBuilder())
            imageLoader.mRequestManager = GlideApp.with(fragmentActivity)
            return imageLoader
        }

        /** 创建Glide加载库 */
        @JvmStatic
        fun with(fragment: Fragment): GlideResource {
            val imageLoader = GlideResource()
            imageLoader.mGlideBuilderBean = getGlideBuilderBean(ImageloaderManager.get().getBuilder())
            imageLoader.mRequestManager = GlideApp.with(fragment)
            return imageLoader
        }

        /** 创建Glide加载库 */
        @JvmStatic
        fun with(view: View): GlideResource {
            val imageLoader = GlideResource()
            imageLoader.mGlideBuilderBean = getGlideBuilderBean(ImageloaderManager.get().getBuilder())
            imageLoader.mRequestManager = GlideApp.with(view)
            return imageLoader
        }

        private fun getGlideBuilderBean(builder: ImageloaderManager.Builder): GlideBuilderBean {
            val bean = GlideBuilderBean()
            if (builder.getPlaceholderResId() != 0) {// 获取配置参数
                bean.placeholderResId = builder.getPlaceholderResId()
            }
            if (builder.getErrorResId() != 0) {
                bean.errorResId = builder.getErrorResId()
            }
            return bean
        }

    }

    override fun loadUrl(url: String): ImageLoaderContract {
        mGlideBuilderBean.path = url
        return GlideImageLoader(mGlideBuilderBean, mRequestManager)
    }

    override fun loadUri(uri: Uri): ImageLoaderContract {
        mGlideBuilderBean.path = uri
        return GlideImageLoader(mGlideBuilderBean, mRequestManager)
    }

    override fun loadFile(file: File): ImageLoaderContract {
        mGlideBuilderBean.path = file
        return GlideImageLoader(mGlideBuilderBean, mRequestManager)
    }

    override fun loadFilePath(path: String): ImageLoaderContract {
        val file = File(path)
        if (file.exists()) {
            mGlideBuilderBean.path = file
            return GlideImageLoader(mGlideBuilderBean, mRequestManager)
        }
        return loadUrl("")
    }

    override fun loadResId(resId: Int): ImageLoaderContract {
        mGlideBuilderBean.path = resId
        return GlideImageLoader(mGlideBuilderBean, mRequestManager)
    }

    override fun loadBase64(base64: String, flags: Int): ImageLoaderContract {
        try {
            return loadBytes(Base64.decode(base64, flags))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return loadUrl("")
    }

    override fun loadBytes(bytes: ByteArray): ImageLoaderContract {
        mGlideBuilderBean.path = bytes
        return GlideImageLoader(mGlideBuilderBean, mRequestManager)
    }
}