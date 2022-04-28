package com.lodz.android.pandora.picker.file

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * 选择器UI配置
 * Created by zhouL on 2018/12/19.
 */
class PickerUIConfig private constructor() {

    companion object {
        @JvmStatic
        fun createDefault(): PickerUIConfig = PickerUIConfig()
    }

    /** 顶部状态栏颜色 */
    @ColorRes
    private var statusBarColor = android.R.color.black
    /** 底部导航栏颜色 */
    @ColorRes
    private var navigationBarColor = android.R.color.black
    /** 拍照按钮图片 */
    @DrawableRes
    private var cameraImg = 0
    /** 拍照按钮背景颜色 */
    @ColorRes
    private var cameraBgColor = android.R.color.black
    /** 照片背景颜色 */
    @ColorRes
    private var itemBgColor = android.R.color.black
    /** 选择按钮未选中颜色 */
    @ColorRes
    private var selectedBtnUnselect = android.R.color.holo_green_dark
    /** 选择按钮选中颜色 */
    @ColorRes
    private var selectedBtnSelected = android.R.color.holo_green_dark
    /** 选中后的遮罩层颜色 */
    @ColorRes
    private var maskColor = 0
    /** 返回按钮颜色 */
    @ColorRes
    private var backBtnColor = android.R.color.white
    /** 主文字颜色 */
    @ColorRes
    private var mainTextColor = android.R.color.white
    /** 更多文件夹图片 */
    @DrawableRes
    private var moreFolderImg = 0
    /** 顶部背景颜色 */
    @ColorRes
    private var topLayoutColor = android.R.color.black
    /** 底部背景颜色 */
    @ColorRes
    private var bottomLayoutColor = android.R.color.black
    /** 预览按钮普通颜色 */
    @ColorRes
    private var previewBtnNormal = android.R.color.white
    /** 预览按钮按压颜色 */
    @ColorRes
    private var previewBtnPressed = android.R.color.darker_gray
    /** 预览按钮不可用颜色 */
    @ColorRes
    private var previewBtnUnable = android.R.color.darker_gray
    /** 确认按钮普通颜色 */
    @ColorRes
    private var confirmBtnNormal = android.R.color.holo_green_light
    /** 确认按钮按压颜色 */
    @ColorRes
    private var confirmBtnPressed = android.R.color.white
    /** 确认按钮不可用颜色 */
    @ColorRes
    private var confirmBtnUnable = android.R.color.holo_green_dark
    /** 确认文字普通颜色 */
    @ColorRes
    private var confirmTextNormal = android.R.color.white
    /** 确认文字按压颜色 */
    @ColorRes
    private var confirmTextPressed = android.R.color.holo_green_dark
    /** 确认文字不可用颜色 */
    @ColorRes
    private var confirmTextUnable = android.R.color.darker_gray
    /** 文件夹选择颜色 */
    @ColorRes
    private var folderSelectColor = android.R.color.holo_green_dark
    /** 预览页背景色 */
    @ColorRes
    private var previewBgColor = android.R.color.black

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            statusBarColor = color
        }
        return this
    }

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            navigationBarColor = color
        }
        return this
    }

    /** 设置相机图标[resId] */
    fun setCameraImg(@DrawableRes resId: Int): PickerUIConfig {
        if (resId != 0) {
            cameraImg = resId
        }
        return this
    }

    /** 设置拍照按钮背景颜色[color] */
    fun setCameraBgColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            cameraBgColor = color
        }
        return this
    }

    /** 设置照片背景颜色[color] */
    fun setItemBgColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            itemBgColor = color
        }
        return this
    }

    /** 设置选择按钮未选中颜色[color] */
    fun setSelectedBtnUnselect(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            selectedBtnUnselect = color
        }
        return this
    }

    /** 设置选择按钮选中颜色[color] */
    fun setSelectedBtnSelected(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            selectedBtnSelected = color
        }
        return this
    }

    /** 设置选中后的遮罩层颜色[color] */
    fun setMaskColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            maskColor = color
        }
        return this
    }

    /** 设置返回按钮颜色[color] */
    fun setBackBtnColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            backBtnColor = color
        }
        return this
    }

    /** 设置主文字颜色[color] */
    fun setMainTextColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            mainTextColor = color
        }
        return this
    }

    /** 设置更多文件夹图片[color] */
    fun setMoreFolderImg(@DrawableRes resId: Int): PickerUIConfig {
        if (resId != 0) {
            moreFolderImg = resId
        }
        return this
    }

    /** 设置顶部背景颜色[color] */
    fun setTopLayoutColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            topLayoutColor = color
        }
        return this
    }

    /** 设置底部背景颜色[color] */
    fun setBottomLayoutColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            bottomLayoutColor = color
        }
        return this
    }

    /** 设置预览按钮普通颜色[color] */
    fun setPreviewBtnNormal(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            previewBtnNormal = color
        }
        return this
    }

    /** 设置预览按钮按压颜色[color] */
    fun setPreviewBtnPressed(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            previewBtnPressed = color
        }
        return this
    }

    /** 设置预览按钮不可用颜色[color] */
    fun setPreviewBtnUnable(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            previewBtnUnable = color
        }
        return this
    }

    /** 设置确认按钮普通颜色[color] */
    fun setConfirmBtnNormal(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            confirmBtnNormal = color
        }
        return this
    }

    /** 设置确认按钮按压颜色[color] */
    fun setConfirmBtnPressed(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            confirmBtnPressed = color
        }
        return this
    }

    /** 设置确认按钮不可用颜色[color] */
    fun setConfirmBtnUnable(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            confirmBtnUnable = color
        }
        return this
    }

    /** 设置确认文字普通颜色[color] */
    fun setConfirmTextNormal(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            confirmTextNormal = color
        }
        return this
    }

    /** 设置确认文字按压颜色[color] */
    fun setConfirmTextPressed(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            confirmTextPressed = color
        }
        return this
    }

    /** 设置确认文字不可用颜色[color] */
    fun setConfirmTextUnable(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            confirmTextUnable = color
        }
        return this
    }

    /** 设置文件夹选择颜色[color] */
    fun setFolderSelectColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            folderSelectColor = color
        }
        return this
    }

    /** 设置预览页背景色[color] */
    fun setPreviewBgColor(@ColorRes color: Int): PickerUIConfig {
        if (color != 0) {
            previewBgColor = color
        }
        return this
    }

    /** 顶部状态栏颜色 */
    @ColorRes
    internal fun getStatusBarColor(): Int = statusBarColor

    /** 底部导航栏颜色 */
    @ColorRes
    internal fun getNavigationBarColor(): Int = navigationBarColor

    /** 拍照按钮图片 */
    @DrawableRes
    internal fun getCameraImg(): Int = cameraImg

    /** 拍照按钮背景颜色 */
    @ColorRes
    internal fun getCameraBgColor(): Int = cameraBgColor

    /** 照片背景颜色 */
    @ColorRes
    internal fun getItemBgColor(): Int = itemBgColor

    /** 选择按钮未选中颜色 */
    @ColorRes
    internal fun getSelectedBtnUnselect(): Int = selectedBtnUnselect

    /** 选择按钮选中颜色 */
    @ColorRes
    internal fun getSelectedBtnSelected(): Int = selectedBtnSelected

    /** 选中后的遮罩层颜色 */
    @ColorRes
    internal fun getMaskColor(): Int = maskColor

    /** 返回按钮颜色 */
    @ColorRes
    internal fun getBackBtnColor(): Int = backBtnColor

    /** 主文字颜色 */
    @ColorRes
    internal fun getMainTextColor(): Int = mainTextColor

    /** 更多文件夹图片 */
    @DrawableRes
    internal fun getMoreFolderImg(): Int = moreFolderImg

    /** 顶部背景颜色 */
    @ColorRes
    internal fun getTopLayoutColor(): Int = topLayoutColor

    /** 底部背景颜色 */
    @ColorRes
    internal fun getBottomLayoutColor(): Int = bottomLayoutColor

    /** 预览按钮普通颜色 */
    @ColorRes
    internal fun getPreviewBtnNormal(): Int = previewBtnNormal

    /** 预览按钮按压颜色 */
    @ColorRes
    internal fun getPreviewBtnPressed(): Int = previewBtnPressed

    /** 预览按钮不可用颜色 */
    @ColorRes
    internal fun getPreviewBtnUnable(): Int = previewBtnUnable

    /** 确认按钮普通颜色 */
    @ColorRes
    internal fun getConfirmBtnNormal(): Int = confirmBtnNormal

    /** 确认按钮按压颜色 */
    @ColorRes
    internal fun getConfirmBtnPressed(): Int = confirmBtnPressed

    /** 确认按钮不可用颜色 */
    @ColorRes
    internal fun getConfirmBtnUnable(): Int = confirmBtnUnable

    /** 确认文字普通颜色 */
    @ColorRes
    internal fun getConfirmTextNormal(): Int = confirmTextNormal

    /** 确认文字按压颜色 */
    @ColorRes
    internal fun getConfirmTextPressed(): Int = confirmTextPressed

    /** 确认文字不可用颜色 */
    @ColorRes
    internal fun getConfirmTextUnable(): Int = confirmTextUnable

    /** 文件夹选择颜色 */
    @ColorRes
    internal fun getFolderSelectColor(): Int = folderSelectColor

    /** 预览页背景色 */
    @ColorRes
    internal fun getPreviewBgColor(): Int = previewBgColor

}