package com.lodz.android.agiledevkt.modules.pic.ninegrid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityNineGridBinding
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.*
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.picker.preview.AbsImageView
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.ninegrid.OnNineGridViewListener
import com.lodz.android.pandora.widget.ninegrid.OnSimpleNineGridViewListener
import permissions.dispatcher.*
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 九宫格测试类
 * Created by zhouL on 2018/12/26.
 */
class NineGridActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NineGridActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityNineGridBinding by bindingLayout(ActivityNineGridBinding::inflate)

    private val hasPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.CAMERA,// 相机
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.pic_nine_grid)
        getTitleBarLayout().elevation = 0f
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermission()//申请权限
    }

    override fun setListeners() {
        super.setListeners()
        // 只显示图片的九宫格
        mBinding.showOnlyNineGridView.setOnNineGridViewListener(object : OnNineGridViewListener {
            override fun onAddPic(addCount: Int) {}

            override fun onDisplayImg(context: Context, data: PicInfo, imageView: ImageView) {
                ImageLoader.create(context).loadUrl(data.path).setCenterCrop().into(imageView)
            }

            override fun onDeletePic(data: PicInfo, position: Int) {}

            override fun onClickPic(data: PicInfo, position: Int) {
                toastShort("click position $position")
            }
        })

        // 获取九宫格路径按钮
        mBinding.onlyPicBtn.setOnClickListener {
            val list = mBinding.showOnlyNineGridView.getPicData()
            var result = ""
            for (path in list) {
                result += path.toString() + "\n\n"
            }
            mBinding.resultTv.text = result
        }

        // 显示拍照
        mBinding.showCameraSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.pickerNineGridView.setNeedCamera(isChecked)
        }

        // 单张预览
        mBinding.itemPreviewSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.pickerNineGridView.setNeedItemPreview(isChecked)
        }

        // 添加图片按钮
        mBinding.addBtnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.pickerNineGridView.setNeedAddBtn(isChecked)
        }

        // 删除图片按钮
        mBinding.deleteBtnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.pickerNineGridView.setShowDelete(isChecked)
        }

        // 允许拖拽
        mBinding.dragSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.pickerNineGridView.setNeedDrag(isChecked)
        }

        // 拖拽震动
        mBinding.dragVibrateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.pickerNineGridView.setNeedDragVibrate(isChecked)
        }

        // 选项组
        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.six_rb -> mBinding.pickerNineGridView.setMaxPic(6)
                R.id.nine_rb -> mBinding.pickerNineGridView.setMaxPic(9)
                R.id.twelve_rb -> mBinding.pickerNineGridView.setMaxPic(12)
            }
        }

        // 可添加图片的九宫格
        mBinding.pickerNineGridView.setOnSimpleNineGridViewListener(object : OnSimpleNineGridViewListener<ImageView> {
            override fun createImageView(): AbsImageView<ImageView, PicInfo> = object : AbsImageView<ImageView, PicInfo>(mBinding.scaleSwitch.isChecked) {
                override fun onCreateView(context: Context, isScale: Boolean): ImageView {
                    val img = if (isScale) PhotoView(context) else ImageView(context)
                    img.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    return img
                }

                override fun onDisplayImg(context: Context, source: PicInfo, view: ImageView) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ImageLoader.create(context).loadUri(source.uri).setFitCenter().into(view)
                    } else {
                        ImageLoader.create(context).loadFilePath(source.path).setFitCenter().into(view)
                    }
                }

                override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: ImageView, item: PicInfo, position: Int, controller: PreviewController) {
                    super.onClickImpl(viewHolder, view, item, position, controller)
                    view.setOnClickListener {
                        if (mBinding.clickClosePreviewSwitch.isChecked) {
                            controller.close()
                        }
                    }
                }

                override fun onViewDetached(view: ImageView, isScale: Boolean) {
                    super.onViewDetached(view, isScale)
                    if (isScale && view is PhotoView) {
                        view.attacher.update()
                    }
                }
            }

            override fun onDisplayNineGridImg(context: Context, data: PicInfo, imageView: ImageView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ImageLoader.create(context).loadUri(data.uri).setCenterCrop().into(imageView)
                } else {
                    ImageLoader.create(context).loadFilePath(data.path).setCenterCrop().into(imageView)
                }
            }

            override fun onDisplayPickerImg(context: Context, data: PicInfo, imageView: ImageView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ImageLoader.create(context).loadUri(data.uri).setCenterCrop().into(imageView)
                } else {
                    ImageLoader.create(context).loadFilePath(data.path).setCenterCrop().into(imageView)
                }
            }
        })

        // 获取九宫格路径按钮
        mBinding.getPicBtn.setOnClickListener {
            val list = mBinding.pickerNineGridView.getPicData()
            var result = ""
            for (path in list) {
                result += path.toString() + "\n\n"
            }
            mBinding.resultTv.text = result
        }
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermission()//申请权限
        } else {
            init()
        }
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            hasPermissions.launch()
            return
        }
        init()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    private fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    private fun onDenied() {
        onRequestPermission()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    private fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        goAppDetailSetting()
        showStatusError()
    }

    private fun init() {
        mBinding.pickerNineGridView.config(FileManager.getCacheFolderPath(), "com.lodz.android.agiledevkt.fileprovider")
        mBinding.showOnlyNineGridView.setStrData(Constant.IMG_URLS.toArrayList())
        showStatusCompleted()
    }
}